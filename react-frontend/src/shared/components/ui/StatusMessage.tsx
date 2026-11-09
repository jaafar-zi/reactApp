import React from 'react';
import { Box } from '@mui/material';

type MessageVariant = 'error' | 'success' | 'warning' | 'info';

const variantStyles: Record<MessageVariant, { bg: string; color: string }> = {
  error: { bg: '#fee2e2', color: '#991b1b' },
  success: { bg: '#d1fae5', color: '#065f46' },
  warning: { bg: '#fef3c7', color: '#92400e' },
  info: { bg: '#dbeafe', color: '#1e40af' },
};

interface StatusMessageProps {
  variant?: MessageVariant;
  message: string;
  visible?: boolean;
}

export const StatusMessage: React.FC<StatusMessageProps> = ({
  variant = 'info',
  message,
  visible = true,
}) => {
  if (!visible || !message) return null;
  const s = variantStyles[variant];

  return (
    <Box
      role="alert"
      aria-live="polite"
      sx={{
        padding: '0.75rem',
        textAlign: 'center',
        fontSize: '0.75rem',
        textTransform: 'uppercase',
        letterSpacing: '0.1em',
        bgcolor: s.bg,
        color: s.color,
      }}
    >
      {message}
    </Box>
  );
};