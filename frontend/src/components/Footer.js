import React from 'react';
import '../styles/Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-section">
          <h3>MediGen</h3>
          <p>AI-Powered Clinical Triage & Administrative Assistant</p>
        </div>
        
        <div className="footer-section">
          <h4>Quick Links</h4>
          <ul>
            <li><a href="/">Patient Registration</a></li>
            <li><a href="/patient-intake">Patient Intake</a></li>
            <li><a href="/doctor-login">Doctor Login</a></li>
          </ul>
        </div>
        
        <div className="footer-section">
          <h4>Technology</h4>
          <ul>
            <li>Java Spring Boot</li>
            <li>React.js</li>
            <li>Google Gemini AI</li>
            <li>MySQL & MongoDB</li>
          </ul>
        </div>
        
        <div className="footer-section">
          <h4>Contact</h4>
          <p>Email: support@medigen.com</p>
          <p>© 2025 MediGen. All rights reserved.</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;

