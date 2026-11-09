import { Typography, Box } from '@mui/material';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../hooks/useAuth';

export default function Home() {
  const { t } = useTranslation('common');
  const { isAuthenticated, user } = useAuth();

  return (
    <Box textAlign="center" mt={8}>
      <Typography variant="h3" gutterBottom>
        {t('appName')}
      </Typography>
      {isAuthenticated && (
        <Typography variant="h5" color="text.secondary">
          {t('messages.welcome', { name: user?.firstName })}
        </Typography>
      )}
    </Box>
  );
}