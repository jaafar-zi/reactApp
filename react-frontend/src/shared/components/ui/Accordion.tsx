import React, { useState } from 'react';
import { Box, Typography } from '@mui/material';

interface AccordionProps {
  itemId: string;
  title: string;
  defaultExpanded?: boolean;
  children: React.ReactNode;
}

export const Accordion: React.FC<AccordionProps> = ({
  itemId,
  title,
  defaultExpanded = false,
  children,
}) => {
  const [expanded, setExpanded] = useState(defaultExpanded);

  return (
    <Box
      sx={{
        borderBottom: '1px solid #e5e7eb',
        '&:first-of-type': { borderTop: '1px solid #e5e7eb' },
      }}
    >
      <button
        type="button"
        onClick={() => setExpanded(!expanded)}
        aria-expanded={expanded}
        aria-controls={`accordion-content-${itemId}`}
        style={{
          width: '100%',
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          padding: expanded ? '0.875rem 0 0.5rem' : '0.875rem 0',
          background: 'none',
          border: 'none',
          cursor: 'pointer',
          textAlign: 'left',
          fontFamily: 'inherit',
          transition: 'opacity 300ms cubic-bezier(0.4, 0, 0.2, 1)',
        }}
        onMouseEnter={e => (e.currentTarget.style.opacity = '0.7')}
        onMouseLeave={e => (e.currentTarget.style.opacity = '1')}
      >
        <Typography
          component="span"
          sx={{
            flex: 1,
            fontSize: '0.75rem',
            fontWeight: 500,
            textTransform: 'uppercase',
            letterSpacing: '0.1em',
          }}
        >
          {title}
        </Typography>

        {/* Plus/Minus icon — CSS only, matches Angular exactly */}
        <Box
          sx={{
            position: 'relative',
            width: 16,
            height: 16,
            flexShrink: 0,
            ml: '1rem',
          }}
        >
          {/* Horizontal line */}
          <Box
            sx={{
              position: 'absolute',
              top: '50%',
              left: 0,
              width: '100%',
              height: 1,
              bgcolor: 'currentColor',
              transform: 'translateY(-50%)',
            }}
          />
          {/* Vertical line — rotates and fades when open */}
          <Box
            sx={{
              position: 'absolute',
              left: '50%',
              top: 0,
              width: 1,
              height: '100%',
              bgcolor: 'currentColor',
              transform: expanded
                ? 'translateX(-50%) rotate(90deg)'
                : 'translateX(-50%)',
              opacity: expanded ? 0 : 1,
              transition:
                'transform 300ms cubic-bezier(0.4, 0, 0.2, 1), opacity 300ms cubic-bezier(0.4, 0, 0.2, 1)',
            }}
          />
        </Box>
      </button>

      <Box
        id={`accordion-content-${itemId}`}
        role="region"
        aria-hidden={!expanded}
        sx={{
          display: expanded ? 'block' : 'none',
          opacity: expanded ? 1 : 0,
          transition: 'opacity 300ms cubic-bezier(0.4, 0, 0.2, 1)',
        }}
      >
        <Box
          sx={{
            pb: '0.875rem',
            fontSize: '0.875rem',
            lineHeight: 1.75,
            color: '#374151',
          }}
        >
          {children}
        </Box>
      </Box>
    </Box>
  );
};