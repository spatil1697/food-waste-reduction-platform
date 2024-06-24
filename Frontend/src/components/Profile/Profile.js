import React, { useState, useEffect } from 'react';
import './profile.css';
import { Link, Outlet, useLocation } from 'react-router-dom';

const Profile = () => {
  const location = useLocation();
  const [activeSection, setActiveSection] = useState('profile');

  useEffect(() => {
    const path = location.pathname.split('/')[2];
    setActiveSection(path || 'profile');
  }, [location]);

  return (
    <section id='Profile' className='profile'>
      <div className="profile-grid">
        <div className="profile-item-left">
          <Link 
            className={activeSection === 'profile' ? 'active' : ''} 
            onClick={() => setActiveSection('profile')} 
            to="/profile"
          >
            My Profile
          </Link>
          <Link 
            className={activeSection === 'my-food-donations' ? 'active' : ''} 
            onClick={() => setActiveSection('my-food-donations')}
            to="/profile/my-food-donations"
          >
            My Food Donations
          </Link>
          <Link 
            className={activeSection === 'delete-account' ? 'active' : ''} 
            onClick={() => setActiveSection('delete-account')}
            to="/profile/delete-account"
          >
            Delete Account
          </Link>
        </div>
        <div className="profile-item-right">
          <Outlet />
        </div>
      </div>
    </section>
  );
};

export default Profile;
