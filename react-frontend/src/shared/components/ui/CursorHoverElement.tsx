import React, { useEffect, useRef, useState, type RefObject } from 'react';
import { Box } from '@mui/material';

interface CursorHoverElementProps {
  isVisible: boolean;
  containerRef?: RefObject<HTMLDivElement | null>;
  referenceText?: string;
  actionText?: string;
}

const CursorHoverElement: React.FC<CursorHoverElementProps> = ({
  isVisible,
  referenceText = '',
  actionText = '',
}) => {
  const [position, setPosition] = useState({ x: 0, y: 0 });
  const targetRef = useRef({ x: 0, y: 0 });
  const currentRef = useRef({ x: 0, y: 0 });
  const rafRef = useRef<number>(0);

  useEffect(() => {
    const handleMouseMove = (e: MouseEvent) => {
      targetRef.current = { x: e.clientX, y: e.clientY };
    };

    const animate = () => {
      const ease = 0.15;
      currentRef.current.x += (targetRef.current.x - currentRef.current.x) * ease;
      currentRef.current.y += (targetRef.current.y - currentRef.current.y) * ease;
      setPosition({ x: currentRef.current.x, y: currentRef.current.y });
      rafRef.current = requestAnimationFrame(animate);
    };

    if (isVisible) {
      window.addEventListener('mousemove', handleMouseMove);
      rafRef.current = requestAnimationFrame(animate);
    }

    return () => {
      window.removeEventListener('mousemove', handleMouseMove);
      cancelAnimationFrame(rafRef.current);
    };
  }, [isVisible]);

  if (!isVisible) return null;

  return (
    <Box
      sx={{
        position: 'fixed',
        left: position.x + 16,
        top: position.y - 12,
        pointerEvents: 'none',
        zIndex: 1000,
        display: 'flex',
        alignItems: 'center',
        gap: 0.5,
      }}
    >
      <svg width="8" height="16" viewBox="0 0 8 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M6 1L1 8L6 15" stroke="currentColor" strokeWidth="1.5" />
      </svg>

      {referenceText && (
        <Box component="span" sx={{
          fontSize: '0.6rem', fontWeight: 500,
          textTransform: 'uppercase', letterSpacing: '0.1em',
          color: 'text.secondary', whiteSpace: 'nowrap',
        }}>
          {referenceText}
        </Box>
      )}

      {actionText && (
        <Box component="span" sx={{
          fontSize: '0.6rem', fontWeight: 400,
          color: 'text.disabled', whiteSpace: 'nowrap',
        }}>
          {actionText}
        </Box>
      )}

      <svg width="8" height="16" viewBox="0 0 8 16" fill="none" xmlns="http://www.w3.org/2000/svg">
        <path d="M2 1L7 8L2 15" stroke="currentColor" strokeWidth="1.5" />
      </svg>
    </Box>
  );
};

export default React.memo(CursorHoverElement);