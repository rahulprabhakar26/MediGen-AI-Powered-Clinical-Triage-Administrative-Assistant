import React from 'react';
import ServiceCard from '../components/ServiceCard';
import '../styles/Home.css';

const Home = () => {
  const services = [
    {
      icon: '👤',
      title: 'Patient Registration',
      description: 'Register as a new patient and create your medical profile',
      link: '/patient-registration',
      color: '#4F46E5'
    },
    {
      icon: '📋',
      title: 'Patient Intake',
      description: 'Submit symptoms and get AI-powered triage assessment',
      link: '/patient-intake',
      color: '#10B981'
    },
    {
      icon: '👨‍⚕️',
      title: 'Doctor Login',
      description: 'Access your dashboard to review patient records',
      link: '/doctor-login',
      color: '#F59E0B'
    },
    {
      icon: '🔐',
      title: 'Doctor Registration',
      description: 'Register as a healthcare professional',
      link: '/doctor-register',
      color: '#EF4444'
    }
  ];

  return (
    <div className="home-container">
      <section className="hero-section">
        <h1 className="hero-title">Welcome to MediGen</h1>
        <p className="hero-subtitle">
          AI-Powered Clinical Triage & Administrative Assistant
        </p>
        <p className="hero-description">
          Streamline healthcare workflows with automated SOAP note generation,
          intelligent triage recommendations, and comprehensive patient management.
        </p>
      </section>

      <section className="services-section">
        <h2 className="section-title">Our Services</h2>
        <div className="services-grid">
          {services.map((service, index) => (
            <ServiceCard key={index} {...service} />
          ))}
        </div>
      </section>

      <section className="features-section">
        <h2 className="section-title">Why Choose MediGen?</h2>
        <div className="features-grid">
          <div className="feature-item">
            <span className="feature-icon">🤖</span>
            <h3>AI-Powered Analysis</h3>
            <p>Automated SOAP note generation using Google Gemini AI</p>
          </div>
          <div className="feature-item">
            <span className="feature-icon">⚡</span>
            <h3>Fast Triage</h3>
            <p>Instant risk assessment: LOW, MEDIUM, or HIGH priority</p>
          </div>
          <div className="feature-item">
            <span className="feature-icon">🔒</span>
            <h3>Secure & Safe</h3>
            <p>JWT authentication and encrypted data storage</p>
          </div>
          <div className="feature-item">
            <span className="feature-icon">💳</span>
            <h3>Payment Integration</h3>
            <p>Seamless payment processing with Razorpay</p>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;

