import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import '../styles/Header.css';

const Header = () => {
  const location = useLocation();

  const isActive = (path) => location.pathname === path;

  return (
    <header className="header">
      <div className="header-container">
        <div className="logo">
          <h1>🏥 MediGen</h1>
          <p className="tagline">AI-Powered Clinical Assistant</p>
        </div>
        
        <nav className="nav-menu">
          <Link 
            to="/" 
            className={`nav-link ${isActive('/') ? 'active' : ''}`}
          >
            Home
          </Link>
          <Link 
            to="/patient-intake" 
            className={`nav-link ${isActive('/patient-intake') ? 'active' : ''}`}
          >
            Patient Intake
          </Link>
          <Link 
            to="/doctor-login" 
            className={`nav-link ${isActive('/doctor-login') ? 'active' : ''}`}
          >
            Doctor Login
          </Link>
          <Link 
            to="/doctor-register" 
            className={`nav-link ${isActive('/doctor-register') ? 'active' : ''}`}
          >
            Doctor Register
          </Link>
          <Link 
            to="/doctor-dashboard" 
            className={`nav-link ${isActive('/doctor-dashboard') ? 'active' : ''}`}
          >
            Dashboard
          </Link>
        </nav>
      </div>
    </header>
  );
};

export default Header;

