import React, { useState } from 'react';
import {
  AppBar,
  Toolbar,
  Box,
  IconButton,
  Drawer,
  List,
  ListItem,
  ListItemButton,
  ListItemText,
  Typography,
  Divider,
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import PersonOutlineIcon from '@mui/icons-material/PersonOutlined';
import CloseIcon from '@mui/icons-material/Close';
import ShoppingBagOutlinedIcon from '@mui/icons-material/ShoppingBagOutlined';
import LogoutIcon from '@mui/icons-material/Logout';
import { Link, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { selectCartCount, toggleCart } from '../../../features/cart/slice/cartSlice';
import { APP_ROUTES } from '../../constants/routes';
import { useAppDispatch, useAppSelector } from '../../../store/hooks';
import { LanguageSwitcher } from '../ui/LanguageSwitcher';
import useAuth from '../../hooks/useAuth';

export const Navbar: React.FC = () => {
  const { t } = useTranslation('common');
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const cartCount = useAppSelector(selectCartCount);
  const [mobileOpen, setMobileOpen] = useState(false);
  const { isAuthenticated, user, logout } = useAuth();

  const handleLogout = () => {
    logout();
    navigate(APP_ROUTES.auth.login);
  };

  const navLinks = [
    { label: t('nav.home'), to: APP_ROUTES.home },
    { label: t('nav.collections'), to: APP_ROUTES.collections.all },
    { label: t('nav.about'), to: APP_ROUTES.about },
  ];

  return (
    <>
      <AppBar
        position="fixed"
        elevation={0}
        sx={{
          bgcolor: '#fff',
          color: '#000',
          borderBottom: '1px solid #e5e7eb',
          zIndex: 100,
        }}
      >
        <Toolbar
          sx={{
            justifyContent: 'space-between',
            px: { xs: '1rem', sm: '2rem' },
            minHeight: { xs: '4.5rem', sm: '5rem', md: '6rem' },
          }}
        >
          {/* Mobile Menu Button */}
          <IconButton
            edge="start"
            onClick={() => setMobileOpen(true)}
            sx={{ display: { md: 'none' } }}
            aria-label="Open menu"
          >
            <MenuIcon />
          </IconButton>

          {/* Desktop Nav Links */}
          <Box
            sx={{
              display: { xs: 'none', md: 'flex' },
              gap: '2rem',
              alignItems: 'center',
            }}
          >
            {navLinks.map(link => (
              <Typography
                key={link.to}
                component={Link}
                to={link.to}
                sx={{
                  fontSize: '0.75rem',
                  fontWeight: 500,
                  textTransform: 'uppercase',
                  letterSpacing: '0.1em',
                  color: '#000',
                  textDecoration: 'none',
                  transition: 'opacity 300ms ease',
                  '&:hover': { opacity: 0.6 },
                }}
              >
                {link.label}
              </Typography>
            ))}
          </Box>

          {/* Logo */}
          <Typography
            component={Link}
            to={APP_ROUTES.home}
            sx={{
              fontSize: '1.25rem',
              fontWeight: 700,
              textTransform: 'uppercase',
              letterSpacing: '0.15em',
              color: '#000',
              textDecoration: 'none',
              position: 'absolute',
              left: '50%',
              transform: 'translateX(-50%)',
            }}
          >
            ORGC
          </Typography>

          {/* Right Actions */}
          <Box sx={{ display: 'flex', alignItems: 'center', gap: '0.5rem' }}>
            <LanguageSwitcher />

            {isAuthenticated ? (
              <>
                <IconButton
                  onClick={() => navigate(APP_ROUTES.profile)}
                  aria-label={t('nav.profile')}
                  sx={{ color: '#000' }}
                >
                  <PersonOutlineIcon fontSize="small" />
                </IconButton>
                <IconButton
                  onClick={handleLogout}
                  aria-label="Logout"
                  sx={{ color: '#000' }}
                >
                  <LogoutIcon fontSize="small" />
                </IconButton>
              </>
            ) : (
              <IconButton
                onClick={() => navigate(APP_ROUTES.auth.login)}
                aria-label={t('nav.profile')}
                sx={{ color: '#000' }}
              >
                <PersonOutlineIcon fontSize="small" />
              </IconButton>
            )}

            <IconButton
              onClick={() => dispatch(toggleCart())}
              aria-label={t('nav.cart')}
              sx={{ color: '#000', position: 'relative' }}
            >
              <ShoppingBagOutlinedIcon fontSize="small" />
              {cartCount > 0 && (
                <Box
                  sx={{
                    position: 'absolute',
                    top: 4,
                    right: 4,
                    bgcolor: '#000',
                    color: '#fff',
                    fontSize: '0.5rem',
                    fontWeight: 700,
                    width: 16,
                    height: 16,
                    borderRadius: '50%',
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                  }}
                >
                  {cartCount}
                </Box>
              )}
            </IconButton>
          </Box>
        </Toolbar>
      </AppBar>

      {/* Mobile Drawer */}
      <Drawer
        anchor="left"
        open={mobileOpen}
        onClose={() => setMobileOpen(false)}
        slotProps={{
          paper: {
            sx: { width: '280px', p: '1rem' },
          },
        }}
      >
        <Box sx={{ display: 'flex', justifyContent: 'flex-end', mb: '1rem' }}>
          <IconButton onClick={() => setMobileOpen(false)}>
            <CloseIcon />
          </IconButton>
        </Box>
        <Divider sx={{ mb: '1rem' }} />
        <List>
          {navLinks.map(link => (
            <ListItem key={link.to} disablePadding>
              <ListItemButton
                component={Link}
                to={link.to}
                onClick={() => setMobileOpen(false)}
              >
                <ListItemText
                  primary={link.label}
                  slotProps={{
                    primary: {
                      sx: {
                        fontSize: '0.875rem',
                        fontWeight: 500,
                        textTransform: 'uppercase',
                        letterSpacing: '0.1em',
                      },
                    },
                  }}
                />
              </ListItemButton>
            </ListItem>
          ))}
          <Divider sx={{ my: '1rem' }} />
          {isAuthenticated ? (
            <>
              <ListItem disablePadding>
                <ListItemButton
                  onClick={() => { navigate(APP_ROUTES.profile); setMobileOpen(false); }}
                >
                  <ListItemText
                    primary={user?.firstName || t('nav.profile')}
                    slotProps={{
                      primary: {
                        sx: {
                          fontSize: '0.875rem',
                          fontWeight: 500,
                          textTransform: 'uppercase',
                          letterSpacing: '0.1em',
                        },
                      },
                    }}
                  />
                </ListItemButton>
              </ListItem>
              <ListItem disablePadding>
                <ListItemButton
                  onClick={() => { handleLogout(); setMobileOpen(false); }}
                >
                  <ListItemText
                    primary="Logout"
                    slotProps={{
                      primary: {
                        sx: {
                          fontSize: '0.875rem',
                          fontWeight: 500,
                          textTransform: 'uppercase',
                          letterSpacing: '0.1em',
                          color: 'error.main',
                        },
                      },
                    }}
                  />
                </ListItemButton>
              </ListItem>
            </>
          ) : (
            <ListItem disablePadding>
              <ListItemButton
                onClick={() => { navigate(APP_ROUTES.auth.login); setMobileOpen(false); }}
              >
                <ListItemText
                  primary="Login"
                  slotProps={{
                    primary: {
                      sx: {
                        fontSize: '0.875rem',
                        fontWeight: 500,
                        textTransform: 'uppercase',
                        letterSpacing: '0.1em',
                      },
                    },
                  }}
                />
              </ListItemButton>
            </ListItem>
          )}
        </List>
      </Drawer>
    </>
  );
};