import React from 'react';
import { Box, IconButton, Menu, MenuItem, Typography } from '@mui/material';
import TranslateIcon from '@mui/icons-material/Translate';
import { useTranslation } from 'react-i18next';
import { selectLang, setLang } from '../../../features/preferences/slice/preferencesSlice';
import { useAppDispatch, useAppSelector } from '../../../store/hooks';
import { type SupportedLang, SUPPORTED_LANGS, LANG_CONFIGS } from '../../constants/preferences.constants';

export const LanguageSwitcher: React.FC = () => {
  const { i18n } = useTranslation();
  const dispatch = useAppDispatch();
  const currentLang = useAppSelector(selectLang);
  const [anchorEl, setAnchorEl] = React.useState<null | HTMLElement>(null);

  const handleOpen = (e: React.MouseEvent<HTMLElement>) => setAnchorEl(e.currentTarget);
  const handleClose = () => setAnchorEl(null);

  const handleChange = (lang: SupportedLang) => {
    dispatch(setLang(lang));
    i18n.changeLanguage(lang);
    handleClose();
  };

  return (
    <>
      <IconButton
        onClick={handleOpen}
        aria-label="Change language"
        sx={{ color: '#000', fontSize: '0.75rem' }}
      >
        <TranslateIcon fontSize="small" />
        <Typography
          sx={{
            ml: '0.25rem',
            fontSize: '0.625rem',
            fontWeight: 500,
            textTransform: 'uppercase',
            display: { xs: 'none', sm: 'inline' },
          }}
        >
          {currentLang}
        </Typography>
      </IconButton>
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleClose}
        slotProps={{
          paper: {
            sx: { borderRadius: 0, mt: '0.5rem', minWidth: '140px' },
          },
        }}
      >
        {SUPPORTED_LANGS.map(lang => {
          const config = LANG_CONFIGS[lang];
          return (
            <MenuItem
              key={lang}
              selected={lang === currentLang}
              onClick={() => handleChange(lang)}
              sx={{
                fontSize: '0.75rem',
                textTransform: 'uppercase',
                letterSpacing: '0.05em',
                gap: '0.5rem',
              }}
            >
              <span>{config.flag}</span>
              <span>{config.nativeLabel}</span>
            </MenuItem>
          );
        })}
      </Menu>
    </>
  );
};