import React from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/ServiceCard.css';

const ServiceCard = ({ icon, title, description, link, color }) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(link);
  };

  return (
    <div className="service-card" onClick={handleClick} style={{ borderTop: `4px solid ${color}` }}>
      <div className="card-icon" style={{ color }}>
        {icon}
      </div>
      <h3 className="card-title">{title}</h3>
      <p className="card-description">{description}</p>
      <button className="card-button" style={{ backgroundColor: color }}>
        Get Started →
      </button>
    </div>
  );
};

export default ServiceCard;

