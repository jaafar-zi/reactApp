import React from 'react';
import { Outlet } from 'react-router-dom';
import { Footer } from './shared/components/layout/Footer';
import { Navbar } from './shared/components/layout/Navbar';

const AppLayout: React.FC = () => (
  <>
    <Navbar />
    <main>
      <Outlet />
    </main>
    <Footer />
  </>
);

export default AppLayout;