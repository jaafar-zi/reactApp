import React from 'react';
import { Box, Typography, Link as MuiLink, IconButton } from '@mui/material';
import InstagramIcon from '@mui/icons-material/Instagram';
import FacebookIcon from '@mui/icons-material/Facebook';
import TwitterIcon from '@mui/icons-material/Twitter';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { APP_ROUTES } from '../../constants/routes';

export const Footer: React.FC = () => {
  const { t } = useTranslation('common');
  const year = new Date().getFullYear();

  return (
    <Box
      component="footer"
      sx={{
        bgcolor: '#111827',
        color: '#fff',
        pt: '4rem',
        pb: '2rem',
        px: { xs: '1rem', sm: '2rem' },
      }}
    >
      <Box
        sx={{
          maxWidth: '100%',
          mx: 'auto',
          display: 'grid',
          gridTemplateColumns: { xs: '1fr', sm: 'repeat(3, 1fr)' },
          gap: '2rem',
          mb: '3rem',
        }}
      >
        {/* Brand */}
        <Box>
          <Typography
            sx={{
              fontSize: '1.25rem',
              fontWeight: 700,
              textTransform: 'uppercase',
              letterSpacing: '0.15em',
              mb: '0.75rem',
            }}
          >
            ORGC
          </Typography>
          <Typography sx={{ fontSize: '0.75rem', color: '#9ca3af', lineHeight: 1.75 }}>
            {t('footer.tagline')}
          </Typography>
          <Box sx={{ display: 'flex', gap: '0.5rem', mt: '1rem' }}>
            <IconButton sx={{ color: '#9ca3af', '&:hover': { color: '#fff' } }} size="small">
              <InstagramIcon fontSize="small" />
            </IconButton>
            <IconButton sx={{ color: '#9ca3af', '&:hover': { color: '#fff' } }} size="small">
              <FacebookIcon fontSize="small" />
            </IconButton>
            <IconButton sx={{ color: '#9ca3af', '&:hover': { color: '#fff' } }} size="small">
              <TwitterIcon fontSize="small" />
            </IconButton>
          </Box>
        </Box>

        {/* Shop Links */}
        <Box>
          <Typography
            sx={{
              fontSize: '0.75rem',
              fontWeight: 500,
              textTransform: 'uppercase',
              letterSpacing: '0.1em',
              mb: '1rem',
            }}
          >
            {t('footer.shop')}
          </Typography>
          {[
            { label: t('footer.allProducts'), to: APP_ROUTES.collections.all },
          ].map(link => (
            <Typography
              key={link.to}
              component={Link}
              to={link.to}
              sx={{
                display: 'block',
                fontSize: '0.75rem',
                color: '#9ca3af',
                textDecoration: 'none',
                mb: '0.5rem',
                '&:hover': { color: '#fff' },
              }}
            >
              {link.label}
            </Typography>
          ))}
        </Box>

        {/* Info Links */}
        <Box>
          <Typography
            sx={{
              fontSize: '0.75rem',
              fontWeight: 500,
              textTransform: 'uppercase',
              letterSpacing: '0.1em',
              mb: '1rem',
            }}
          >
            {t('footer.info')}
          </Typography>
          {[
            { label: t('footer.aboutUs'), to: APP_ROUTES.about },
          ].map(link => (
            <Typography
              key={link.to}
              component={Link}
              to={link.to}
              sx={{
                display: 'block',
                fontSize: '0.75rem',
                color: '#9ca3af',
                textDecoration: 'none',
                mb: '0.5rem',
                '&:hover': { color: '#fff' },
              }}
            >
              {link.label}
            </Typography>
          ))}
        </Box>
      </Box>

      {/* Bottom Bar */}
      <Box
        sx={{
          borderTop: '1px solid #1f2937',
          pt: '1.5rem',
          textAlign: 'center',
        }}
      >
        <Typography sx={{ fontSize: '0.625rem', color: '#6b7280', letterSpacing: '0.05em' }}>
          © {year} ORGC. {t('footer.rights')}
        </Typography>
      </Box>
    </Box>
  );
};