import React, { useState } from 'react';
import { useProfile } from '../../context/ProfileContext';
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css';
import { MdAssignmentAdd } from 'react-icons/md';
import './myFoodDonations.css'
import {Link} from 'react-router-dom';


const MyFoodDonations = () => {
  return (
    <section id="my-food-donations" className="my-food-donations"> 
    <h2>Food Donations</h2>
      <div className="advertise-donation">
      <div className="donation-button">
        <div>
            <MdAssignmentAdd color="var(--color-bg-variant)" size="19px" /> 
            <Link to="/advertise" className="donation-text">Advertise new Donation</Link>
        </div>
    </div>
      </div>
      <div className="profile-input">
      </div>     
    </section>
  )
}

export default MyFoodDonations