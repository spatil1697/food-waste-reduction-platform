import React, { useState } from 'react';
import { useProfile } from '../../context/ProfileContext';
import PhoneInput from 'react-phone-input-2';
import 'react-phone-input-2/lib/style.css';
import { MdAssignmentAdd } from 'react-icons/md';
import './my-food-donations.css'


const MyFoodDonations = () => {
  return (
    <section id="my-food-donations" className="my-food-donations"> 
    <h2>Food Donations</h2>
      <div className="advertise-donation">
      <div className="donation-button">
        <div>
            <MdAssignmentAdd color="var(--color-bg-variant)" size="19px" /> 
            <span className="donation-text">Advertise new Donation</span>
        </div>
    </div>
      </div>
      <div className="profile-input">
      </div>     
    </section>
  )
}

export default MyFoodDonations