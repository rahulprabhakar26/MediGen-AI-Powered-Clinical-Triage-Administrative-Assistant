import React, { useState } from 'react';
import api from '../utils/api';

const PatientIntake = () => {
  const [formData, setFormData] = useState({
    patientId: localStorage.getItem('patientId') || '',
    symptoms: '',
    description: '',
    medicalHistory: '',
    currentMedications: '',
    allergies: '',
    vitalSigns: ''
  });
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState('');
  const [result, setResult] = useState(null);

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
      const response = await api.post('/api/patients/records', {
        ...formData,
        patientId: parseInt(formData.patientId)
      });
      
      setResult(response.data);
      setMessage('Patient record created successfully!');
    } catch (error) {
      console.error('Record creation error:', error);
      setMessage('Failed to create record: ' + (error.response?.data?.error || error.response?.data?.message || error.message));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="card">
        <h2>📋 Patient Intake Form</h2>
        <p style={{ textAlign: 'center', color: '#718096', marginBottom: '30px' }}>
          Fill out your medical information for AI-powered clinical assessment
        </p>
        {message && (
          <div className={`alert ${message.includes('successfully') ? 'alert-success' : 'alert-error'}`}>
            {message}
          </div>
        )}
        
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <label>Patient ID</label>
            <input
              type="number"
              name="patientId"
              value={formData.patientId}
              onChange={handleChange}
              required
            />
          </div>

          <div className="form-group">
            <label>Symptoms</label>
            <textarea
              name="symptoms"
              value={formData.symptoms}
              onChange={handleChange}
              required
              rows="3"
              placeholder="Describe the main symptoms..."
            />
          </div>

          <div className="form-group">
            <label>Detailed Description</label>
            <textarea
              name="description"
              value={formData.description}
              onChange={handleChange}
              required
              rows="4"
              placeholder="Provide detailed description of the condition..."
            />
          </div>

          <div className="form-group">
            <label>Medical History</label>
            <textarea
              name="medicalHistory"
              value={formData.medicalHistory}
              onChange={handleChange}
              rows="3"
              placeholder="Previous medical conditions, surgeries, etc."
            />
          </div>

          <div className="form-group">
            <label>Current Medications</label>
            <textarea
              name="currentMedications"
              value={formData.currentMedications}
              onChange={handleChange}
              rows="2"
              placeholder="List current medications..."
            />
          </div>

          <div className="form-group">
            <label>Allergies</label>
            <textarea
              name="allergies"
              value={formData.allergies}
              onChange={handleChange}
              rows="2"
              placeholder="Known allergies..."
            />
          </div>

          <div className="form-group">
            <label>Vital Signs</label>
            <textarea
              name="vitalSigns"
              value={formData.vitalSigns}
              onChange={handleChange}
              rows="2"
              placeholder="Blood pressure, temperature, heart rate, etc."
            />
          </div>

          <button type="submit" className="btn" disabled={loading}>
            {loading ? '⏳ Processing with AI...' : '🤖 Submit for AI Analysis'}
          </button>
        </form>
      </div>

      {result && (
        <div className="card">
          <h3>🧠 AI-Generated Clinical Summary</h3>
          <div className="form-group">
            <label>Triage Level</label>
            <div className={`triage-${result.triageLevel?.toLowerCase()}`} style={{ 
              padding: '10px 20px', 
              borderRadius: '8px', 
              textAlign: 'center',
              fontSize: '18px',
              fontWeight: 'bold',
              marginBottom: '20px'
            }}>
              {result.triageLevel} Priority
            </div>
          </div>
          
          <div className="form-group">
            <label>AI-Generated SOAP Notes</label>
            <textarea
              value={result.aiGeneratedNotes || 'No notes generated'}
              readOnly
              rows="15"
              style={{ 
                backgroundColor: '#f8f9fa', 
                border: '2px solid #e2e8f0',
                borderRadius: '12px',
                padding: '16px',
                fontFamily: 'monospace',
                fontSize: '14px',
                lineHeight: '1.5'
              }}
            />
          </div>
        </div>
      )}
    </div>
  );
};

export default PatientIntake;
