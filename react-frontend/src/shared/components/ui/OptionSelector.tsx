import React from 'react';
import { Box, Typography } from '@mui/material';

interface OptionSelectorProps {
  options: string[];
  selected: string | null;
  label?: string;
  disabled?: boolean;
  onChange: (option: string) => void;
}

export const OptionSelector: React.FC<OptionSelectorProps> = ({
  options,
  selected,
  label,
  disabled = false,
  onChange,
}) => (
  <Box sx={{ py: '1rem' }}>
    {label && (
      <Typography
        sx={{
          fontSize: '0.75rem',
          fontWeight: 500,
          textTransform: 'uppercase',
          letterSpacing: '0.1em',
          mb: '0.75rem',
          color: '#000',
        }}
      >
        {label}
      </Typography>
    )}
    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: '0.5rem' }}>
      {options.map(option => {
        const isSelected = selected === option;
        return (
          <button
            key={option}
            type="button"
            disabled={disabled}
            onClick={() => !disabled && onChange(option)}
            style={{
              padding: '0.75rem 1.5rem',
              fontSize: '0.75rem',
              textTransform: 'uppercase',
              letterSpacing: '0.1em',
              border: `1px solid ${isSelected ? '#000' : '#d1d5db'}`,
              background: isSelected ? '#000' : '#fff',
              color: isSelected ? '#fff' : '#000',
              cursor: disabled ? 'not-allowed' : 'pointer',
              opacity: disabled ? 0.4 : 1,
              textDecoration: disabled ? 'line-through' : 'none',
              transition: 'all 300ms cubic-bezier(0.4, 0, 0.2, 1)',
              fontFamily: 'inherit',
            }}
          >
            {option}
          </button>
        );
      })}
    </Box>
  </Box>
);