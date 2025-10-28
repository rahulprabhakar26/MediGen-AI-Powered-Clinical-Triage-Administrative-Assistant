import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../utils/api';

const DoctorDashboard = () => {
  const navigate = useNavigate();
  const [records, setRecords] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedRecord, setSelectedRecord] = useState(null);
  const [doctorNotes, setDoctorNotes] = useState('');
  const [message, setMessage] = useState('');

  useEffect(() => {
    const token = localStorage.getItem('doctorToken');
    if (!token) {
      navigate('/doctor-login');
      return;
    }
    fetchPatientRecords();
  }, [navigate]);

  const fetchPatientRecords = async () => {
    try {
      const response = await api.get('/api/patients/records');
      setRecords(response.data || []);
    } catch (error) {
      console.error('Failed to fetch records:', error);
      setMessage('Failed to load records: ' + (error.response?.data?.error || error.message));
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateRecord = async (recordId) => {
    try {
      const doctorIdValue = localStorage.getItem('doctorId');
      console.log('Updating record with:', { recordId, doctorNotes, doctorId: doctorIdValue });
      
      await api.put(`/api/patients/records/${recordId}`, null, {
        params: {
          doctorNotes: doctorNotes,
          doctorId: doctorIdValue ? Number(doctorIdValue) : null
        }
      });
      setMessage('Record updated successfully!');
      setSelectedRecord(null);
      setDoctorNotes('');
      fetchPatientRecords();
    } catch (error) {
      console.error('Update error:', error);
      console.error('Error response:', error.response?.data);
      setMessage('Failed to update record: ' + (error.response?.data?.message || error.response?.data?.error || error.message));
    }
  };

  const getTriageClass = (level) => {
    return `triage-${level?.toLowerCase()}`;
  };

  if (loading) {
    return <div className="loading">Loading patient records...</div>;
  }

  return (
    <div>
      <div className="card">
        <h2>👨‍⚕️ Doctor Dashboard</h2>
        <p style={{ textAlign: 'center', color: '#718096', marginBottom: '30px' }}>
          Review patient records and AI-generated clinical assessments
        </p>
        {message && (
          <div className={`alert ${message.includes('successfully') ? 'alert-success' : 'alert-error'}`}>
            {message}
          </div>
        )}
        
        <table className="table">
          <thead>
            <tr>
              <th>Patient ID</th>
              <th>Symptoms</th>
              <th>Triage Level</th>
              <th>Created Date</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {records.map((record) => (
              <tr key={record.id}>
                <td>{record.patient?.id}</td>
                <td>{record.symptoms}</td>
                <td className={getTriageClass(record.triageLevel)}>
                  {record.triageLevel}
                </td>
                <td>{new Date(record.createdAt).toLocaleDateString()}</td>
                <td>
                  <button
                    className="btn btn-secondary"
                    onClick={() => setSelectedRecord(record)}
                  >
                    👁️ View Details
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {selectedRecord && (
        <div className="card">
          <h3>📋 Patient Record Details</h3>
          
          <div className="form-group">
            <label><strong>Patient ID:</strong> {selectedRecord.patient?.id}</label>
          </div>
          
          <div className="form-group">
            <label><strong>Symptoms:</strong></label>
            <p>{selectedRecord.symptoms}</p>
          </div>
          
          <div className="form-group">
            <label><strong>Description:</strong></label>
            <p>{selectedRecord.description}</p>
          </div>
          
          <div className="form-group">
            <label><strong>Triage Level:</strong></label>
            <div className={getTriageClass(selectedRecord.triageLevel)}>
              {selectedRecord.triageLevel}
            </div>
          </div>
          
          <div className="form-group">
            <label><strong>AI Generated Notes:</strong></label>
            <textarea
              value={selectedRecord.aiGeneratedNotes || 'No AI notes available'}
              readOnly
              rows="8"
              style={{ backgroundColor: '#f8f9fa' }}
            />
          </div>
          
          <div className="form-group">
            <label><strong>Doctor Notes:</strong></label>
            <textarea
              value={doctorNotes}
              onChange={(e) => setDoctorNotes(e.target.value)}
              rows="4"
              placeholder="Add your clinical notes here..."
            />
          </div>
          
          <div style={{ display: 'flex', gap: '10px' }}>
            <button
              className="btn btn-success"
              onClick={() => handleUpdateRecord(selectedRecord.id)}
            >
              💾 Update Record
            </button>
            <button
              className="btn btn-secondary"
              onClick={() => {
                setSelectedRecord(null);
                setDoctorNotes('');
              }}
            >
              ❌ Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default DoctorDashboard;
