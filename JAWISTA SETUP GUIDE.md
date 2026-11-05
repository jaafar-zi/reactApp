JAWISTA SETUP GUIDE
===================

1. PREREQUISITES
Make sure the following are installed on the server:

- Java 17
- Maven
- Node.js 20
- npm
- PostgreSQL
- Nginx
- Certbot

Verify:

java -version
mvn -version
node -v
npm -v
psql --version
nginx -v

2. COPY PROJECT
Transfer the project folder to the server using a USB drive,
SCP, SFTP, or any file transfer method available.

Copy to:

/var/www/jawista

Expected structure after copy:

/var/www/jawista
├── lightweight-ecommerce
├── jawista-frontend
├── uploads
└── .env

IMPORTANT:
- The uploads folder is already included in the project files.
- Do not delete it.
- Do not recreate it manually.
- Copy the project as-is and keep the uploads folder intact.

If using SCP from your local machine:

scp -r /path/to/deploymentFolder/* user@SERVER_IP:/var/www/jawista/

If the server has no internet access, use a USB drive or
internal network file transfer.

3. DATABASE SETUP
Login to PostgreSQL:

su - postgres
psql

Create user and database:

CREATE USER jawista_user WITH PASSWORD 'your_strong_password';
CREATE DATABASE jawista_db OWNER jawista_user;
GRANT ALL PRIVILEGES ON DATABASE jawista_db TO jawista_user;
\q
exit

Verify the connection works:

psql -U jawista_user -d jawista_db -h localhost

If you see the jawista_db=# prompt, the database is ready.
Type \q to exit.

No manual SQL import is needed.
The backend will automatically create all tables and
populate required data on first start.

4. CREATE ENV FILE
Create the environment file:

nano /var/www/jawista/.env

Paste and fill in your values:

SPRING_PROFILE=prod
DB_URL=jdbc:postgresql://localhost:5432/jawista_db
DB_USERNAME=jawista_user
DB_PASSWORD=your_strong_password
DB_POOL_SIZE=10

SERVER_PORT=8080
CONTEXT_PATH=/

JWT_SECRET=generate_with_openssl_rand_base64_32
JWT_EXPIRATION_MS=86400000
BASIC_AUTH_USERNAME=admin
BASIC_AUTH_PASSWORD=your_admin_password

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=jawistaclub@gmail.com
MAIL_PASSWORD=ccaq yezy ghqz opaj
MAIL_SMTP_AUTH=true
MAIL_SMTP_STARTTLS=true
MAIL_SMTP_STARTTLS_REQUIRED=false
MAIL_DEBUG=false
MAIL_ENABLED=true
MAIL_FROM_EMAIL=jawistaclub@gmail.com
MAIL_FROM_NAME=Jawista Store

FRONTEND_URL=https://www.jawista.com
COMPANY_NAME=Jawista Club
SUPPORT_EMAIL=jawistaclub@gmail.com
APP_DEFAULT_LANGUAGE=en
APP_SUPPORTED_LANGUAGES=en,fr,es,de

STORAGE_BASE_DIR=/var/www/jawista/uploads
STORAGE_MAX_FILE_SIZE=52428800
MEDIA_BASE_URL=/media

JPA_SHOW_SQL=false

LOG_LEVEL_ROOT=INFO
LOG_LEVEL_APP=INFO
LOG_LEVEL_SPRING_WEB=WARN
LOG_LEVEL_SPRING_SECURITY=WARN
LOG_LEVEL_HIBERNATE=WARN
LOG_LEVEL_HIBERNATE_SQL=WARN
LOG_LEVEL_HIBERNATE_BINDER=WARN
LOG_FILE=logs/application.log
LOG_FILE_MAX_SIZE=10MB
LOG_FILE_MAX_HISTORY=30

THYMELEAF_CACHE=true

Generate JWT secret:

openssl rand -base64 32

Copy the output and paste it as the JWT_SECRET value.

Protect the file:

chmod 600 /var/www/jawista/.env

Gmail App Password:
MAIL_PASSWORD is NOT your Gmail login password.
To generate one:
  1. Go to myaccount.google.com
  2. Security > 2-Step Verification (must be ON)
  3. Security > App Passwords
  4. Create new > Mail
  5. Copy the 16-character password into MAIL_PASSWORD

5. SET PERMISSIONS

chown -R www-data:www-data /var/www/jawista
chmod -R 755 /var/www/jawista
chmod -R 755 /var/www/jawista/uploads

6. BUILD BACKEND

cd /var/www/jawista/lightweight-ecommerce
mvn clean package -DskipTests

First run may take a few minutes while Maven downloads dependencies.
If the server has no internet access, dependencies must be
pre-downloaded and bundled before transfer.

Verify the build succeeded:

ls target/*.jar

You should see a .jar file listed.

7. BUILD FRONTEND

cd /var/www/jawista/jawista-frontend
npm ci
npm run build

Verify:

ls dist/jawista-front-e-trunk/server/server.mjs

If the file exists the build is complete.

8. BACKEND SERVICE
Create the service file:

nano /etc/systemd/system/jawista-backend.service

Paste:

[Unit]
Description=Jawista Backend
After=network.target postgresql.service
Requires=postgresql.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/var/www/jawista/lightweight-ecommerce
EnvironmentFile=/var/www/jawista/.env
ExecStart=/usr/bin/java -jar /var/www/jawista/lightweight-ecommerce/target/ecommerce-backend-1.0.0.jar
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target

9. FRONTEND SERVICE
Create the service file:

nano /etc/systemd/system/jawista-frontend.service

Paste:

[Unit]
Description=Jawista Frontend
After=network.target jawista-backend.service
Requires=jawista-backend.service

[Service]
Type=simple
User=www-data
WorkingDirectory=/var/www/jawista/jawista-frontend
Environment=NODE_ENV=production
Environment=PORT=4000
ExecStart=/usr/bin/node -e "import('./dist/jawista-front-e-trunk/server/server.mjs').then(m => m.run())"
Restart=on-failure
RestartSec=10

[Install]
WantedBy=multi-user.target

10. ENABLE AND START SERVICES

systemctl daemon-reload
systemctl enable jawista-backend
systemctl enable jawista-frontend
systemctl start jawista-backend
systemctl start jawista-frontend

Check status:

systemctl status jawista-backend
systemctl status jawista-frontend

Watch backend startup (wait for "Started EcommerceApplication"):

journalctl -u jawista-backend -f

Press Ctrl+C to stop watching.

Verify backend is healthy:

curl http://localhost:8080/actuator/health

Expected: {"status":"UP"}

11. NGINX
Create the site config:

nano /etc/nginx/sites-available/jawista

Paste:

upstream frontend {
    server 127.0.0.1:4000;
}

upstream backend {
    server 127.0.0.1:8080;
}

server {
    listen 80;
    server_name jawista.com www.jawista.com;

    client_max_body_size 50M;

    location /api/ {
        proxy_pass http://backend;
        proxy_http_version 1.1;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }

    location /media/ {
        alias /var/www/jawista/uploads/;
        expires 30d;
        add_header Cache-Control "public";
        try_files $uri =404;
    }

    location /uploads/ {
        alias /var/www/jawista/uploads/;
        expires 30d;
        add_header Cache-Control "public";
        try_files $uri =404;
    }

    location / {
        proxy_pass http://frontend;
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection 'upgrade';
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}

Replace jawista.com www.jawista.com with your actual domain.
If using IP only during initial setup:

    server_name YOUR_SERVER_IP;

Enable the site:

ln -s /etc/nginx/sites-available/jawista /etc/nginx/sites-enabled/jawista
rm -f /etc/nginx/sites-enabled/default
nginx -t
systemctl reload nginx

Test:

curl -I http://YOUR_SERVER_IP

Expected: HTTP/1.1 200 OK

12. SSL
Only after domain DNS is pointed to this server.

Verify DNS first:

dig +short www.jawista.com

Output must be the server IP. If not, wait for DNS propagation.

Get certificate:

certbot --nginx -d jawista.com -d www.jawista.com

When asked about redirect, choose option 2 (HTTPS redirect).

Test auto-renewal:

certbot renew --dry-run

After SSL is active, update the env file:

nano /var/www/jawista/.env

Make sure:

FRONTEND_URL=https://www.jawista.com

Then restart backend:

systemctl restart jawista-backend

13. USEFUL COMMANDS

Restart all:
systemctl restart jawista-backend jawista-frontend nginx

Backend logs:
journalctl -u jawista-backend -f

Frontend logs:
journalctl -u jawista-frontend -f

Nginx errors:
tail -f /var/log/nginx/error.log

Health check:
curl http://localhost:8080/actuator/health

14. NOTES
- The uploads folder is provided and must not be recreated manually.
- MEDIA_BASE_URL must stay /media after going live.
- STORAGE_BASE_DIR must be /var/www/jawista/uploads.
- FRONTEND_URL must use the real public domain with https://.
- Gmail password must be a Gmail App Password.
- If the server has no internet access during build, Maven and npm
  dependencies must be bundled before transfer.
- Services are set to restart automatically on crash or server reboot.