import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../utils/api';

const DoctorLogin = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: ''
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMessage('');

    try {
      const response = await api.post('/api/auth/login', formData);
      localStorage.setItem('doctorToken', response.data.token);
      localStorage.setItem('doctorId', response.data.doctorId);
      localStorage.setItem('doctorEmail', response.data.email);
      localStorage.setItem('doctorName', `${response.data.firstName} ${response.data.lastName}`);
      setMessage('Login successful!');
      setTimeout(() => {
        navigate('/doctor-dashboard');
      }, 1000);
    } catch (error) {
      console.error('Login error:', error);
      setMessage('Login failed: ' + (error.response?.data?.error || error.response?.data?.message || error.message));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>🔐 Doctor Login</h2>
      <p style={{ textAlign: 'center', color: '#718096', marginBottom: '30px' }}>
        Access your dashboard to review patient records and AI-generated clinical notes
      </p>
      {message && (
        <div className={`alert ${message.includes('successful') ? 'alert-success' : 'alert-error'}`}>
          {message}
        </div>
      )}
      
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Email</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Password</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit" className="btn" disabled={loading}>
          {loading ? '⏳ Logging in...' : '🚀 Login'}
        </button>
      </form>
      
      <p style={{ marginTop: '20px', textAlign: 'center', color: '#718096' }}>
        Don't have an account? <Link to="/doctor-register">Register here</Link>
      </p>
    </div>
  );
};

export default DoctorLogin;
