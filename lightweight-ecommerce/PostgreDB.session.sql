-- ============================================================
-- JAWISTA E-COMMERCE DATABASE POPULATION SCRIPT
-- Collection 01 — El Mediterraneo
-- PostgreSQL
-- ============================================================

-- Clean existing data (children before parents)
TRUNCATE TABLE shipping_statuses CASCADE;
TRUNCATE TABLE shippings CASCADE;
TRUNCATE TABLE order_items CASCADE;
TRUNCATE TABLE orders CASCADE;
TRUNCATE TABLE inventory CASCADE;
TRUNCATE TABLE product_pricing CASCADE;
TRUNCATE TABLE product_assets CASCADE;
TRUNCATE TABLE product_attributes CASCADE;
TRUNCATE TABLE products CASCADE;
TRUNCATE TABLE addresses CASCADE;
TRUNCATE TABLE subscribers CASCADE;

-- Reset sequences
ALTER SEQUENCE products_id_seq RESTART WITH 1;
ALTER SEQUENCE product_attributes_id_seq RESTART WITH 1;
ALTER SEQUENCE product_assets_id_seq RESTART WITH 1;
ALTER SEQUENCE product_pricing_id_seq RESTART WITH 1;
ALTER SEQUENCE inventory_id_seq RESTART WITH 1;
ALTER SEQUENCE addresses_id_seq RESTART WITH 1;
ALTER SEQUENCE orders_id_seq RESTART WITH 1;
ALTER SEQUENCE order_items_id_seq RESTART WITH 1;
ALTER SEQUENCE shippings_id_seq RESTART WITH 1;
ALTER SEQUENCE shipping_statuses_id_seq RESTART WITH 1;
ALTER SEQUENCE subscribers_id_seq RESTART WITH 1;

-- ============================================================
-- PRODUCTS
-- ============================================================
INSERT INTO products (sku, name_key, description_key, active, created_at, updated_at)
VALUES
    ('JW-TSH-WHT-001', 'product.tshirt.chill-bro.white.name',        'product.tshirt.chill-bro.white.desc',        true, NOW(), NOW()),
    ('JW-BAG-BLK-001', 'product.bag.flow-bag.black.name',            'product.bag.flow-bag.black.desc',            true, NOW(), NOW()),
    ('JW-RKT-NAT-001', 'product.racket.club-set.natural.name',       'product.racket.club-set.natural.desc',       true, NOW(), NOW()),
    ('JW-RKT-NAT-002', 'product.racket.pro-set.natural.name',        'product.racket.pro-set.natural.desc',        true, NOW(), NOW()),
    ('JW-BLL-BLK-001', 'product.ball.rally-pack.black.name',         'product.ball.rally-pack.black.desc',         true, NOW(), NOW());

-- ============================================================
-- PRODUCT ATTRIBUTES — ENGLISH
-- ============================================================

ALTER TABLE product_attributes ALTER COLUMN attribute_value TYPE TEXT;

-- While we're at it, these may also hit 255 limits with long content:
ALTER TABLE product_assets ALTER COLUMN url TYPE TEXT;
ALTER TABLE product_assets ALTER COLUMN alt_text TYPE TEXT;

ALTER TABLE orders ALTER COLUMN shipping_address TYPE TEXT;
ALTER TABLE orders ALTER COLUMN billing_address TYPE TEXT;


-- -------------------------------------------------------
-- Product 1: Chill Bro T-shirt [White]
-- -------------------------------------------------------
INSERT INTO product_attributes (product_id, attribute_key, attribute_value, language) VALUES
(1, 'name',        'Chill Bro T-shirt [White]', 'en'),
(1, 'description', 'Our newest white T-shirt arrives in a relaxed, signature Jawista fit — made for everyday ease. The front is understated, detailed with a subtle navy embroidery at the chest. On the back, a bold graphic reminds us: not everything needs a reaction — just slow down and live the moment.', 'en'),
(1, 'collection',  'Collection 01 El Mediterraneo', 'en'),
(1, 'tagline',     'Locally made | Limited drop.', 'en'),
(1, 'color',       'White', 'en'),
(1, 'material',    'Premium 100% Cotton Fabric', 'en'),
(1, 'branding',    'Screen Print & Embroidery Branding', 'en'),
(1, 'origin',      'Locally designed & produced in Tunisia', 'en'),

-- Wash care
(1, 'wash_care_washing_1', 'Machine wash cold with like colors', 'en'),
(1, 'wash_care_washing_2', 'Gentle cycle', 'en'),
(1, 'wash_care_washing_3', 'Use mild detergent', 'en'),
(1, 'wash_care_washing_4', 'Do not bleach', 'en'),
(1, 'wash_care_drying_1',  'Tumble dry low / hang to dry', 'en'),
(1, 'wash_care_drying_2',  'Warm iron if needed (not on branding)', 'en'),
(1, 'wash_care_drying_3',  'Do not dry clean', 'en');

-- -------------------------------------------------------
-- Product 2: Jawista Flow Bag [Black]
-- -------------------------------------------------------
INSERT INTO product_attributes (product_id, attribute_key, attribute_value, language) VALUES
(2, 'name',        'Jawista Flow Bag [Black]', 'en'),
(2, 'description', 'Built for movement, designed for simplicity. This multifunctional black tote bag reflects Jawista''s way of living — flexible, intentional, and easy. Designed to be carried three ways — shoulder, back, or hand — and elevated by a distinctive cotton rope handle. A minimalist piece that simplifies your day while elevating your presence.', 'en'),
(2, 'collection',  'Collection 01 El Mediterraneo', 'en'),
(2, 'tagline',     'Locally made | Limited drop.', 'en'),
(2, 'color',       'Black', 'en'),
(2, 'material',    'Serge Fabric Construction', 'en'),
(2, 'handle',      '100% Cotton Rope Handle', 'en'),
(2, 'design',      'Minimalist Functional Design', 'en'),
(2, 'origin',      'Locally designed & produced in Tunisia', 'en'),

-- Wash care
(2, 'wash_care_washing_1', 'Wash cold with similar colors', 'en'),
(2, 'wash_care_washing_2', 'Gentle cycle', 'en'),
(2, 'wash_care_washing_3', 'Use mild detergent', 'en'),
(2, 'wash_care_washing_4', 'Do not bleach', 'en'),
(2, 'wash_care_drying_1',  'Tumble dry low or hang to dry', 'en'),
(2, 'wash_care_drying_2',  'Warm iron if needed (avoid rope & details)', 'en'),
(2, 'wash_care_drying_3',  'Do not dry clean', 'en');

-- -------------------------------------------------------
-- Product 3: Kids & Beginners Beach Racket Set — Jawista Club [Natural wood]
-- -------------------------------------------------------
INSERT INTO product_attributes (product_id, attribute_key, attribute_value, language) VALUES
(3, 'name',        'Kids & Beginners Beach Racket Set - Jawista Club [Natural wood]', 'en'),
(3, 'description', 'Designed for first swings, shared moments, and easy fun under the sun. The Jawista Club set includes two lightweight plywood rackets, built to maximize exchanges and keep the game flowing. Soft rubber handles provide a comfortable grip, making it easy to play longer without effort. Paired with two Jawista balls, this set is made for smooth rallies and instant enjoyment. Lightweight and easy to handle, it''s the perfect entry point for beginners, families, and kids — helping everyone build confidence, improve coordination, and enjoy the game before stepping up to more advanced play. A simple way to start. A natural way to progress.', 'en'),
(3, 'collection',  'Collection 01 El Mediterraneo', 'en'),
(3, 'tagline',     'Locally made | Limited drop.', 'en'),
(3, 'color',       'Natural wood', 'en'),
(3, 'material',    'Lightweight Plywood Rackets', 'en'),
(3, 'weight',      '≈170g each', 'en'),
(3, 'dimensions',  '38 × 23.5 cm', 'en'),
(3, 'includes_rackets', '2 Rackets Included', 'en'),
(3, 'grip',        'Soft Rubber Grip Handles', 'en'),
(3, 'includes_balls',   '2 Jawista Balls Included', 'en'),
(3, 'origin',      'Locally designed & produced in Tunisia', 'en');

-- -------------------------------------------------------
-- Product 4: Advanced Beach Racket Set – Jawista Pro [Natural wood]
-- -------------------------------------------------------
INSERT INTO product_attributes (product_id, attribute_key, attribute_value, language) VALUES
(4, 'name',        'Advanced Beach Racket Set – Jawista Pro [Natural wood]', 'en'),
(4, 'description', 'Designed for more control, stronger impact, and a refined playing experience under the sun. The Jawista Pro set features two rackets crafted from marine plywood, engineered for durability, precision, and improved inertia. The refined wood processing enhances balance and responsiveness, allowing for cleaner shots and more controlled exchanges. Built for players ready to elevate their game, each detail is designed to deliver a more consistent and dynamic feel. Paired with two Jawista balls, this set supports longer rallies, sharper control, and a more intentional way of playing — whether you''re improving your technique or playing at a higher level. Balanced, resistant, and performance-driven, it''s the natural next step after your first rallies. A step forward in the game. A deeper way to play.', 'en'),
(4, 'collection',  'Collection 01 — El Mediterraneo', 'en'),
(4, 'tagline',     'Locally made | Limited drop.', 'en'),
(4, 'color',       'Natural wood', 'en'),
(4, 'material',    'Lightweight Marine Plywood Rackets', 'en'),
(4, 'weight',      '≈200g each', 'en'),
(4, 'processing',  'Precision wood selection & racket processing', 'en'),
(4, 'dimensions',  '38 × 23.5 cm', 'en'),
(4, 'includes_rackets', 'Set of 2 Rackets', 'en'),
(4, 'grip',        'Soft Rubber Grip Handles for enhanced control', 'en'),
(4, 'includes_balls',   'Includes 2 Jawista Balls', 'en'),
(4, 'origin',      'Locally designed & produced in Tunisia', 'en');

-- -------------------------------------------------------
-- Product 5: Jawista Ball Set [Black]
-- -------------------------------------------------------
INSERT INTO product_attributes (product_id, attribute_key, attribute_value, language) VALUES
(5, 'name',        'Jawista Ball Set [Black]', 'en'),
(5, 'description', 'Small essentials. Maximum play. The Jawista Rally Pack brings together 3 beach tennis balls designed for smooth rallies, consistent bounce, and effortless play on sand. Lightweight and durable, they''re made to keep the game flowing whether you''re practicing, playing casually, or pushing longer exchanges. Each pack comes in a compact, easy-carry mini bag — perfect to keep your balls together, protected, and always ready for the next match. Simple, practical, and made for everyday play.', 'en'),
(5, 'collection',  'Collection 01 — El Mediterraneo', 'en'),
(5, 'tagline',     'Locally made.', 'en'),
(5, 'color',       'Black', 'en'),
(5, 'quantity',    '3 Jawista Beach Tennis Balls', 'en'),
(5, 'thickness',   '4mm', 'en'),
(5, 'bounce',      'Consistent bounce for smooth play', 'en'),
(5, 'build',       'Lightweight & durable design', 'en'),
(5, 'branding',    'Printed Jawista logo on each ball', 'en'),
(5, 'includes_bag','Includes carry bag', 'en'),
(5, 'origin',      'Locally designed & produced in Tunisia', 'en');

-- ============================================================
-- PRODUCT ATTRIBUTES — FRENCH
-- ============================================================

INSERT INTO product_attributes (product_id, attribute_key, attribute_value, language) VALUES
-- Product 1
(1, 'name',        'T-shirt Chill Bro [Blanc]', 'fr'),
(1, 'description', 'Notre nouveau t-shirt blanc arrive dans une coupe décontractée, signature Jawista — conçu pour le confort au quotidien. Le devant est sobre, détaillé par une broderie marine subtile au niveau de la poitrine. Au dos, un graphique audacieux nous rappelle : tout ne mérite pas une réaction — ralentis et vis le moment.', 'fr'),

-- Product 2
(2, 'name',        'Jawista Flow Bag [Noir]', 'fr'),
(2, 'description', 'Conçu pour le mouvement, pensé pour la simplicité. Ce sac fourre-tout noir multifonctionnel reflète l''art de vivre Jawista — flexible, intentionnel et facile. Conçu pour être porté de trois façons — épaule, dos ou main — et sublimé par une anse en corde de coton distinctive.', 'fr'),

-- Product 3
(3, 'name',        'Set de Raquettes de Plage Enfants & Débutants - Jawista Club [Bois naturel]', 'fr'),
(3, 'description', 'Conçu pour les premiers coups, les moments partagés et le plaisir facile sous le soleil. Le set Jawista Club comprend deux raquettes légères en contreplaqué, construites pour maximiser les échanges et garder le jeu fluide.', 'fr'),

-- Product 4
(4, 'name',        'Set de Raquettes de Plage Avancé – Jawista Pro [Bois naturel]', 'fr'),
(4, 'description', 'Conçu pour plus de contrôle, un impact plus fort et une expérience de jeu raffinée sous le soleil. Le set Jawista Pro est doté de deux raquettes en contreplaqué marin, conçues pour la durabilité, la précision et une meilleure inertie.', 'fr'),

-- Product 5
(5, 'name',        'Set de Balles Jawista [Noir]', 'fr'),
(5, 'description', 'Petits essentiels. Jeu maximum. Le Jawista Rally Pack réunit 3 balles de beach tennis conçues pour des échanges fluides, un rebond régulier et un jeu sans effort sur le sable.', 'fr');

-- ============================================================
-- PRODUCT ATTRIBUTES — ARABIC
-- ============================================================

INSERT INTO product_attributes (product_id, attribute_key, attribute_value, language) VALUES
-- Product 1
(1, 'name',        'تي شيرت تشيل برو [أبيض]', 'ar'),
(1, 'description', 'أحدث تي شيرت أبيض بقصة مريحة من جويستا — مصمم للراحة اليومية. الأمام بسيط مع تطريز بحري دقيق على الصدر. في الخلف، رسم جريء يذكرنا: ليس كل شيء يحتاج ردة فعل — فقط تمهل وعش اللحظة.', 'ar'),

-- Product 2
(2, 'name',        'حقيبة جويستا فلو [أسود]', 'ar'),
(2, 'description', 'مصممة للحركة، مبنية على البساطة. حقيبة توت سوداء متعددة الاستخدامات تعكس أسلوب حياة جويستا — مرنة، مقصودة، وسهلة. مصممة لتُحمل بثلاث طرق — على الكتف، الظهر، أو باليد.', 'ar'),

-- Product 3
(3, 'name',        'طقم مضارب شاطئ للمبتدئين والأطفال - جويستا كلوب [خشب طبيعي]', 'ar'),
(3, 'description', 'مصمم للضربات الأولى، اللحظات المشتركة، والمرح السهل تحت الشمس. يتضمن طقم جويستا كلوب مضربين خفيفين من الخشب الرقائقي مصممين لتحقيق أقصى تبادل والحفاظ على تدفق اللعبة.', 'ar'),

-- Product 4
(4, 'name',        'طقم مضارب شاطئ متقدم – جويستا برو [خشب طبيعي]', 'ar'),
(4, 'description', 'مصمم لمزيد من التحكم، تأثير أقوى، وتجربة لعب راقية تحت الشمس. يتميز طقم جويستا برو بمضربين مصنوعين من الخشب الرقائقي البحري، مصممين للمتانة والدقة.', 'ar'),

-- Product 5
(5, 'name',        'طقم كرات جويستا [أسود]', 'ar'),
(5, 'description', 'أساسيات صغيرة. لعب بأقصى حد. يجمع جويستا رالي باك 3 كرات تنس شاطئي مصممة لتبادلات سلسة وارتداد متسق ولعب سهل على الرمال.', 'ar');

-- ============================================================
-- PRODUCT ASSETS
-- ============================================================
INSERT INTO product_assets (product_id, asset_type, url, alt_text, display_order, is_primary)
VALUES
-- Chill Bro T-shirt [White]
(1, 'IMAGE', '/assets/images/products/tshirt-chill-bro-white-front.jpg',    'Chill Bro T-shirt White - Front view',            0, true),
(1, 'IMAGE', '/assets/images/products/tshirt-chill-bro-white-back.jpg',     'Chill Bro T-shirt White - Back graphic view',      1, false),
(1, 'IMAGE', '/assets/images/products/tshirt-chill-bro-white-detail.jpg',   'Chill Bro T-shirt White - Embroidery detail',      2, false),
(1, 'IMAGE', '/assets/images/products/tshirt-chill-bro-white-lifestyle.jpg','Chill Bro T-shirt White - Lifestyle shot',          3, false),

-- Jawista Flow Bag [Black]
(2, 'IMAGE', '/assets/images/products/flow-bag-black-front.jpg',            'Jawista Flow Bag Black - Front view',              0, true),
(2, 'IMAGE', '/assets/images/products/flow-bag-black-shoulder.jpg',         'Jawista Flow Bag Black - Shoulder carry',          1, false),
(2, 'IMAGE', '/assets/images/products/flow-bag-black-back-carry.jpg',       'Jawista Flow Bag Black - Back carry',              2, false),
(2, 'IMAGE', '/assets/images/products/flow-bag-black-rope-detail.jpg',      'Jawista Flow Bag Black - Cotton rope handle detail',3, false),
(2, 'IMAGE', '/assets/images/products/flow-bag-black-lifestyle.jpg',        'Jawista Flow Bag Black - Lifestyle shot',          4, false),

-- Jawista Club Racket Set [Natural wood]
(3, 'IMAGE', '/assets/images/products/racket-club-natural-set.jpg',         'Jawista Club Racket Set - Full set view',          0, true),
(3, 'IMAGE', '/assets/images/products/racket-club-natural-front.jpg',       'Jawista Club Racket Set - Racket front',           1, false),
(3, 'IMAGE', '/assets/images/products/racket-club-natural-grip.jpg',        'Jawista Club Racket Set - Rubber grip detail',     2, false),
(3, 'IMAGE', '/assets/images/products/racket-club-natural-balls.jpg',       'Jawista Club Racket Set - Included balls',         3, false),
(3, 'IMAGE', '/assets/images/products/racket-club-natural-lifestyle.jpg',   'Jawista Club Racket Set - Beach lifestyle shot',   4, false),

-- Jawista Pro Racket Set [Natural wood]
(4, 'IMAGE', '/assets/images/products/racket-pro-natural-set.jpg',          'Jawista Pro Racket Set - Full set view',           0, true),
(4, 'IMAGE', '/assets/images/products/racket-pro-natural-front.jpg',        'Jawista Pro Racket Set - Racket front',            1, false),
(4, 'IMAGE', '/assets/images/products/racket-pro-natural-wood-detail.jpg',  'Jawista Pro Racket Set - Marine plywood detail',   2, false),
(4, 'IMAGE', '/assets/images/products/racket-pro-natural-grip.jpg',         'Jawista Pro Racket Set - Enhanced grip detail',    3, false),
(4, 'IMAGE', '/assets/images/products/racket-pro-natural-lifestyle.jpg',    'Jawista Pro Racket Set - Action lifestyle shot',   4, false),

-- Jawista Ball Set [Black]
(5, 'IMAGE', '/assets/images/products/ball-set-black-pack.jpg',             'Jawista Ball Set Black - Full pack view',          0, true),
(5, 'IMAGE', '/assets/images/products/ball-set-black-single.jpg',           'Jawista Ball Set Black - Single ball detail',      1, false),
(5, 'IMAGE', '/assets/images/products/ball-set-black-logo.jpg',             'Jawista Ball Set Black - Logo print detail',       2, false),
(5, 'IMAGE', '/assets/images/products/ball-set-black-bag.jpg',              'Jawista Ball Set Black - Carry bag',               3, false);

-- ============================================================
-- PRODUCT PRICING (Prices in TND — Tunisian Dinar)
-- ============================================================
INSERT INTO product_pricing (product_id, base_price, sale_price, currency, tax_rate)
VALUES
    (1,  89.00,  NULL,  'TND', 19.00),   -- Chill Bro T-shirt
    (2, 119.00,  NULL,  'TND', 19.00),   -- Jawista Flow Bag
    (3, 149.00,  NULL,  'TND', 19.00),   -- Jawista Club Racket Set
    (4, 199.00,  NULL,  'TND', 19.00),   -- Jawista Pro Racket Set
    (5,  49.00,  NULL,  'TND', 19.00);   -- Jawista Ball Set

-- ============================================================
-- INVENTORY
-- ============================================================
INSERT INTO inventory (product_id, quantity, reserved_quantity, min_stock_level, last_updated)
VALUES
    (1,  100,  5, 15, NOW()),   -- Chill Bro T-shirt
    (2,   60,  3, 10, NOW()),   -- Jawista Flow Bag
    (3,   40,  2,  8, NOW()),   -- Jawista Club Racket Set
    (4,   30,  1,  5, NOW()),   -- Jawista Pro Racket Set
    (5,  120,  8, 20, NOW());   -- Jawista Ball Set

-- ============================================================
-- ADDRESSES
-- ============================================================
INSERT INTO addresses (
    full_name, phone_number, address_line1, address_line2,
    city, state, postal_code, country, type,
    is_default, customer_email, notes, created_at, updated_at
)
VALUES
-- Customer 1: Ahmed Ben Ali
('Ahmed Ben Ali',    '+216-50-123-456', '15 Rue de Marseille',    'Apt 3',        'Tunis',       'Tunis',     '1000', 'TUN', 'BOTH',     true,  'ahmed.benali@email.com',   NULL,                        NOW(), NOW()),

-- Customer 2: Yasmine Trabelsi
('Yasmine Trabelsi', '+216-55-789-012', '42 Avenue Habib Bourguiba', NULL,       'Sousse',      'Sousse',    '4000', 'TUN', 'SHIPPING', true,  'yasmine.trabelsi@email.com', 'Appeler avant livraison', NOW(), NOW()),
('Yasmine Trabelsi', '+216-55-789-012', '8 Rue Ibn Khaldoun',       'Bureau 12',  'Sousse',      'Sousse',    '4000', 'TUN', 'BILLING',  false, 'yasmine.trabelsi@email.com', NULL,                      NOW(), NOW()),

-- Customer 3: Mehdi Khelifi
('Mehdi Khelifi',    '+216-92-456-789', '7 Rue de la Liberté',      NULL,         'Hammamet',    'Nabeul',    '8050', 'TUN', 'BOTH',     true,  'mehdi.khelifi@email.com',  'Près du café central',      NOW(), NOW()),

-- Customer 4: Ines Bouazizi
('Ines Bouazizi',    '+216-98-321-654', '23 Avenue de Carthage',     NULL,        'La Marsa',    'Tunis',     '2078', 'TUN', 'BOTH',     true,  'ines.bouazizi@email.com',  NULL,                        NOW(), NOW()),

-- Customer 5: Sami Gharbi
('Sami Gharbi',      '+216-20-654-987', '5 Rue de Paris',           'Étage 2',    'Sfax',        'Sfax',      '3000', 'TUN', 'SHIPPING', true,  'sami.gharbi@email.com',    'Sonner 2 fois',             NOW(), NOW()),

-- Customer 6: International — Paris
('Karim Mansour',    '+33-6-12-34-56-78','14 Rue du Faubourg Saint-Honoré', NULL, 'Paris',       'Île-de-France','75008','FRA','BOTH',   true,  'karim.mansour@email.com',  NULL,                        NOW(), NOW());

-- ============================================================
-- ORDERS
-- ============================================================
INSERT INTO orders (
    order_number, customer_email, customer_name, status,
    subtotal, tax, total, currency,
    shipping_address, billing_address,
    created_at, updated_at
)
VALUES
-- Order 1: Ahmed — T-shirt + Ball Set — DELIVERED
(
    'JW-ORD-2025-000001', 'ahmed.benali@email.com', 'Ahmed Ben Ali', 'DELIVERED',
    138.00, 26.22, 164.22, 'TND',
    '15 Rue de Marseille, Apt 3, Tunis, Tunis 1000, TUN',
    '15 Rue de Marseille, Apt 3, Tunis, Tunis 1000, TUN',
    NOW() - INTERVAL '25 days', NOW() - INTERVAL '20 days'
),
-- Order 2: Yasmine — Club Racket Set — SHIPPED
(
    'JW-ORD-2025-000002', 'yasmine.trabelsi@email.com', 'Yasmine Trabelsi', 'SHIPPED',
    149.00, 28.31, 177.31, 'TND',
    '42 Avenue Habib Bourguiba, Sousse, Sousse 4000, TUN',
    '8 Rue Ibn Khaldoun, Bureau 12, Sousse, Sousse 4000, TUN',
    NOW() - INTERVAL '5 days', NOW() - INTERVAL '2 days'
),
-- Order 3: Mehdi — Pro Racket Set + Ball Set — PROCESSING
(
    'JW-ORD-2025-000003', 'mehdi.khelifi@email.com', 'Mehdi Khelifi', 'PROCESSING',
    248.00, 47.12, 295.12, 'TND',
    '7 Rue de la Liberté, Hammamet, Nabeul 8050, TUN',
    '7 Rue de la Liberté, Hammamet, Nabeul 8050, TUN',
    NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day'
),
-- Order 4: Ines — Flow Bag + T-shirt — CONFIRMED
(
    'JW-ORD-2025-000004', 'ines.bouazizi@email.com', 'Ines Bouazizi', 'CONFIRMED',
    208.00, 39.52, 247.52, 'TND',
    '23 Avenue de Carthage, La Marsa, Tunis 2078, TUN',
    '23 Avenue de Carthage, La Marsa, Tunis 2078, TUN',
    NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day'
),
-- Order 5: Sami — Pro Racket Set — PENDING
(
    'JW-ORD-2025-000005', 'sami.gharbi@email.com', 'Sami Gharbi', 'PENDING',
    199.00, 37.81, 236.81, 'TND',
    '5 Rue de Paris, Étage 2, Sfax, Sfax 3000, TUN',
    '5 Rue de Paris, Étage 2, Sfax, Sfax 3000, TUN',
    NOW() - INTERVAL '6 hours', NOW() - INTERVAL '6 hours'
),
-- Order 6: Karim (Paris) — Club Racket Set + Ball Set — SHIPPED (International)
(
    'JW-ORD-2025-000006', 'karim.mansour@email.com', 'Karim Mansour', 'SHIPPED',
    198.00, 37.62, 235.62, 'TND',
    '14 Rue du Faubourg Saint-Honoré, Paris, Île-de-France 75008, FRA',
    '14 Rue du Faubourg Saint-Honoré, Paris, Île-de-France 75008, FRA',
    NOW() - INTERVAL '8 days', NOW() - INTERVAL '4 days'
),
-- Order 7: Ahmed — repeat customer — Flow Bag — DELIVERED
(
    'JW-ORD-2025-000007', 'ahmed.benali@email.com', 'Ahmed Ben Ali', 'DELIVERED',
    119.00, 22.61, 141.61, 'TND',
    '15 Rue de Marseille, Apt 3, Tunis, Tunis 1000, TUN',
    '15 Rue de Marseille, Apt 3, Tunis, Tunis 1000, TUN',
    NOW() - INTERVAL '15 days', NOW() - INTERVAL '10 days'
),
-- Order 8: Yasmine — cancelled order — T-shirt — CANCELLED
(
    'JW-ORD-2025-000008', 'yasmine.trabelsi@email.com', 'Yasmine Trabelsi', 'CANCELLED',
    89.00, 16.91, 105.91, 'TND',
    '42 Avenue Habib Bourguiba, Sousse, Sousse 4000, TUN',
    '8 Rue Ibn Khaldoun, Bureau 12, Sousse, Sousse 4000, TUN',
    NOW() - INTERVAL '12 days', NOW() - INTERVAL '11 days'
);

-- ============================================================
-- ORDER ITEMS
-- ============================================================
INSERT INTO order_items (order_id, product_id, quantity, unit_price, total_price)
VALUES
-- Order 1: T-shirt + Ball Set
(1, 1, 1,  89.00,  89.00),
(1, 5, 1,  49.00,  49.00),

-- Order 2: Club Racket Set
(2, 3, 1, 149.00, 149.00),

-- Order 3: Pro Racket Set + Ball Set
(3, 4, 1, 199.00, 199.00),
(3, 5, 1,  49.00,  49.00),

-- Order 4: Flow Bag + T-shirt
(4, 2, 1, 119.00, 119.00),
(4, 1, 1,  89.00,  89.00),

-- Order 5: Pro Racket Set
(5, 4, 1, 199.00, 199.00),

-- Order 6: Club Racket Set + Ball Set (International)
(6, 3, 1, 149.00, 149.00),
(6, 5, 1,  49.00,  49.00),

-- Order 7: Flow Bag
(7, 2, 1, 119.00, 119.00),

-- Order 8: T-shirt (cancelled)
(8, 1, 1,  89.00,  89.00);

-- ============================================================
-- SHIPPINGS
-- ============================================================
INSERT INTO shippings (
    tracking_number, order_id, shipping_address_id,
    shipping_method, current_status, carrier,
    shipping_cost, weight,
    estimated_delivery_date, actual_delivery_date, shipped_date,
    special_instructions, notes,
    created_at, updated_at
)
VALUES
-- Shipping for Order 1 — DELIVERED (local Tunis)
(
    'JW-TRK-2025-000001', 1, 1,
    'STANDARD', 'DELIVERED', 'Aramex',
    7.00, 0.50,
    NOW() - INTERVAL '20 days', NOW() - INTERVAL '21 days', NOW() - INTERVAL '23 days',
    NULL, 'Livré avec succès',
    NOW() - INTERVAL '25 days', NOW() - INTERVAL '20 days'
),
-- Shipping for Order 2 — IN_TRANSIT (Sousse)
(
    'JW-TRK-2025-000002', 2, 2,
    'EXPRESS', 'IN_TRANSIT', 'Aramex',
    12.00, 1.20,
    NOW() + INTERVAL '1 day', NULL, NOW() - INTERVAL '2 days',
    'Appeler avant livraison', NULL,
    NOW() - INTERVAL '5 days', NOW() - INTERVAL '1 day'
),
-- Shipping for Order 3 — PROCESSING (Hammamet)
(
    'JW-TRK-2025-000003', 3, 4,
    'STANDARD', 'PROCESSING', 'Aramex',
    7.00, 1.80,
    NOW() + INTERVAL '4 days', NULL, NULL,
    'Près du café central', 'En attente de ramassage',
    NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day'
),
-- Shipping for Order 6 — IN_TRANSIT (International Paris)
(
    'JW-TRK-2025-000006', 6, 7,
    'INTERNATIONAL', 'IN_TRANSIT', 'DHL',
    35.00, 1.30,
    NOW() + INTERVAL '5 days', NULL, NOW() - INTERVAL '4 days',
    NULL, 'Expédition internationale — douane en cours',
    NOW() - INTERVAL '8 days', NOW() - INTERVAL '2 days'
),
-- Shipping for Order 7 — DELIVERED (local Tunis, repeat customer)
(
    'JW-TRK-2025-000007', 7, 1,
    'STANDARD', 'DELIVERED', 'Aramex',
    7.00, 0.80,
    NOW() - INTERVAL '11 days', NOW() - INTERVAL '11 days', NOW() - INTERVAL '13 days',
    NULL, 'Livré en main propre',
    NOW() - INTERVAL '15 days', NOW() - INTERVAL '10 days'
);

-- ============================================================
-- SHIPPING STATUSES
-- ============================================================

-- Shipping 1 (Order 1) — Full Delivery Lifecycle
INSERT INTO shipping_statuses (shipping_id, status, location, timestamp, description, notes, updated_by, created_at)
VALUES
(1, 'PENDING',          'Tunis, Tunisie',        NOW() - INTERVAL '25 days',           'Commande reçue et en cours de préparation', NULL,                  'SYSTEM',       NOW() - INTERVAL '25 days'),
(1, 'PROCESSING',       'Tunis, Tunisie',        NOW() - INTERVAL '24 days 12 hours',  'Commande en cours d''emballage',            NULL,                  'warehouse_tn', NOW() - INTERVAL '24 days'),
(1, 'READY_TO_SHIP',    'Tunis, Tunisie',        NOW() - INTERVAL '23 days 6 hours',   'Colis prêt pour le ramassage',              NULL,                  'warehouse_tn', NOW() - INTERVAL '23 days'),
(1, 'PICKED_UP',        'Tunis, Tunisie',        NOW() - INTERVAL '23 days',           'Colis récupéré par Aramex',                 NULL,                  'Aramex',       NOW() - INTERVAL '23 days'),
(1, 'IN_TRANSIT',       'Tunis, Tunisie',        NOW() - INTERVAL '22 days',           'Colis en transit',                          NULL,                  'Aramex',       NOW() - INTERVAL '22 days'),
(1, 'OUT_FOR_DELIVERY', 'Tunis, Tunisie',        NOW() - INTERVAL '21 days 4 hours',   'Colis en cours de livraison',               NULL,                  'Aramex',       NOW() - INTERVAL '21 days'),
(1, 'DELIVERED',        'Tunis, Tunisie',        NOW() - INTERVAL '21 days',           'Colis livré au destinataire',               'Signé par: A. Ben Ali','Aramex',       NOW() - INTERVAL '21 days');

-- Shipping 2 (Order 2) — In Transit
INSERT INTO shipping_statuses (shipping_id, status, location, timestamp, description, notes, updated_by, created_at)
VALUES
(2, 'PENDING',     'Tunis, Tunisie',  NOW() - INTERVAL '5 days',          'Commande reçue',                    NULL, 'SYSTEM',       NOW() - INTERVAL '5 days'),
(2, 'PROCESSING',  'Tunis, Tunisie',  NOW() - INTERVAL '4 days',          'Commande en emballage',             NULL, 'warehouse_tn', NOW() - INTERVAL '4 days'),
(2, 'PICKED_UP',   'Tunis, Tunisie',  NOW() - INTERVAL '2 days',          'Récupéré par Aramex',               NULL, 'Aramex',       NOW() - INTERVAL '2 days'),
(2, 'IN_TRANSIT',  'Sousse, Tunisie', NOW() - INTERVAL '1 day',           'En transit vers Sousse',            NULL, 'Aramex',       NOW() - INTERVAL '1 day');

-- Shipping 3 (Order 3) — Processing
INSERT INTO shipping_statuses (shipping_id, status, location, timestamp, description, notes, updated_by, created_at)
VALUES
(3, 'PENDING',    'Tunis, Tunisie', NOW() - INTERVAL '2 days',           'Commande reçue',            NULL, 'SYSTEM',       NOW() - INTERVAL '2 days'),
(3, 'PROCESSING', 'Tunis, Tunisie', NOW() - INTERVAL '1 day 12 hours',  'En cours de préparation',   NULL, 'warehouse_tn', NOW() - INTERVAL '1 day');

-- Shipping 4 (Order 6) — International In Transit
INSERT INTO shipping_statuses (shipping_id, status, location, timestamp, description, notes, updated_by, created_at)
VALUES
(4, 'PENDING',       'Tunis, Tunisie',        NOW() - INTERVAL '8 days',  'Commande internationale reçue',                    NULL,                       'SYSTEM',       NOW() - INTERVAL '8 days'),
(4, 'PROCESSING',    'Tunis, Tunisie',        NOW() - INTERVAL '7 days',  'Préparation pour expédition internationale',       NULL,                       'warehouse_tn', NOW() - INTERVAL '7 days'),
(4, 'READY_TO_SHIP', 'Tunis, Tunisie',        NOW() - INTERVAL '5 days',  'Prêt pour DHL',                                    NULL,                       'warehouse_tn', NOW() - INTERVAL '5 days'),
(4, 'PICKED_UP',     'Tunis, Tunisie',        NOW() - INTERVAL '4 days',  'Récupéré par DHL',                                 NULL,                       'DHL',          NOW() - INTERVAL '4 days'),
(4, 'IN_TRANSIT',    'Paris, France',         NOW() - INTERVAL '2 days',  'Arrivé au pays de destination — dédouanement',     'En attente de douane',     'DHL',          NOW() - INTERVAL '2 days');

-- Shipping 5 (Order 7) — Delivered (repeat customer)
INSERT INTO shipping_statuses (shipping_id, status, location, timestamp, description, notes, updated_by, created_at)
VALUES
(5, 'PENDING',          'Tunis, Tunisie', NOW() - INTERVAL '15 days',          'Commande reçue',                       NULL,                    'SYSTEM',       NOW() - INTERVAL '15 days'),
(5, 'PROCESSING',       'Tunis, Tunisie', NOW() - INTERVAL '14 days',          'En emballage',                         NULL,                    'warehouse_tn', NOW() - INTERVAL '14 days'),
(5, 'PICKED_UP',        'Tunis, Tunisie', NOW() - INTERVAL '13 days',          'Récupéré par Aramex',                  NULL,                    'Aramex',       NOW() - INTERVAL '13 days'),
(5, 'IN_TRANSIT',       'Tunis, Tunisie', NOW() - INTERVAL '12 days',          'En transit local',                     NULL,                    'Aramex',       NOW() - INTERVAL '12 days'),
(5, 'OUT_FOR_DELIVERY', 'Tunis, Tunisie', NOW() - INTERVAL '11 days 4 hours',  'En cours de livraison',                NULL,                    'Aramex',       NOW() - INTERVAL '11 days'),
(5, 'DELIVERED',        'Tunis, Tunisie', NOW() - INTERVAL '11 days',          'Livré en main propre',                 'Signé par: A. Ben Ali', 'Aramex',       NOW() - INTERVAL '11 days');

-- ============================================================
-- SUBSCRIBERS
-- ============================================================
INSERT INTO subscribers (name, email, active, subscribed_at, unsubscribed_at)
VALUES
('Ahmed Ben Ali',      'ahmed.benali@email.com',       true,  NOW() - INTERVAL '60 days',  NULL),
('Yasmine Trabelsi',   'yasmine.trabelsi@email.com',   true,  NOW() - INTERVAL '45 days',  NULL),
('Mehdi Khelifi',      'mehdi.khelifi@email.com',      true,  NOW() - INTERVAL '30 days',  NULL),
('Ines Bouazizi',      'ines.bouazizi@email.com',      true,  NOW() - INTERVAL '20 days',  NULL),
('Sami Gharbi',        'sami.gharbi@email.com',        true,  NOW() - INTERVAL '10 days',  NULL),
('Karim Mansour',      'karim.mansour@email.com',      true,  NOW() - INTERVAL '15 days',  NULL),
('Nour Haddad',        'nour.haddad@email.com',        true,  NOW() - INTERVAL '8 days',   NULL),
('Rim Chaabane',       'rim.chaabane@email.com',       true,  NOW() - INTERVAL '5 days',   NULL),
('Omar Jerbi',         'omar.jerbi@email.com',         false, NOW() - INTERVAL '90 days',  NOW() - INTERVAL '20 days'),
('Leila Slim',         'leila.slim@email.com',         true,  NOW() - INTERVAL '3 days',   NULL),
('Walid Mahjoub',      'walid.mahjoub@email.com',      true,  NOW() - INTERVAL '2 days',   NULL),
('Amira Belhadj',      'amira.belhadj@email.com',      false, NOW() - INTERVAL '120 days', NOW() - INTERVAL '50 days'));

-- ============================================================
-- VERIFICATION
-- ============================================================
DO $$
BEGIN
    RAISE NOTICE '========================================';
    RAISE NOTICE '  JAWISTA — DATA POPULATION SUMMARY    ';
    RAISE NOTICE '  Collection 01 — El Mediterraneo      ';
    RAISE NOTICE '========================================';
    RAISE NOTICE 'Products:           %', (SELECT COUNT(*) FROM products);
    RAISE NOTICE 'Product Attributes: %', (SELECT COUNT(*) FROM product_attributes);
    RAISE NOTICE 'Product Assets:     %', (SELECT COUNT(*) FROM product_assets);
    RAISE NOTICE 'Product Pricing:    %', (SELECT COUNT(*) FROM product_pricing);
    RAISE NOTICE 'Inventory:          %', (SELECT COUNT(*) FROM inventory);
    RAISE NOTICE 'Addresses:          %', (SELECT COUNT(*) FROM addresses);
    RAISE NOTICE 'Orders:             %', (SELECT COUNT(*) FROM orders);
    RAISE NOTICE 'Order Items:        %', (SELECT COUNT(*) FROM order_items);
    RAISE NOTICE 'Shippings:          %', (SELECT COUNT(*) FROM shippings);
    RAISE NOTICE 'Shipping Statuses:  %', (SELECT COUNT(*) FROM shipping_statuses);
    RAISE NOTICE 'Subscribers:        %', (SELECT COUNT(*) FROM subscribers);
    RAISE NOTICE '========================================';
END $$;