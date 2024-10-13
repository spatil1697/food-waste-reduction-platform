import React, { useState, useRef } from 'react';
import './advertise.css';
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import DefaultImage from '../../assets/upload_button_image.png';
import EditIcon from '../../assets/upload_button_image.png';
import { useAdvertise } from '../../context/AdvertiseContext';

const Advertise = () => {
  const [errors, setErrors] = useState({});
  const [startDate, setStartDate] = useState(new Date());
  const [imageURL, setImageURL] = useState(DefaultImage);
  const fileUploadRef = useRef();
  const { advertise, updateAdvertiseData, saveAdvertiseData } = useAdvertise();

  const advertiseFields = [
    { label: 'Food Item Name*', name: 'foodItemName', placeholder: 'Enter the food item name', error: 'Please enter the food item name.', required: true },
    { label: 'Description', name: 'description', placeholder: 'Enter the description', error: 'Please enter the description.', required: false },
    { label: 'Pickup Location*', name: 'pickupLocation', placeholder: 'Enter the pickup location', error: 'Please enter the pickup location.', required: true },
  ];  

  const validateFields = () => {
    const newErrors = {};

  advertiseFields.forEach(field => {
      if (field.required && !advertise[field.name]) {
        newErrors[field.name] = `${field.error}`;
      }
    });  
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleImageUpload = (e) => {
    e.preventDefault();
    fileUploadRef.current.click();
  };

  const uploadImageDisplay = () => {
    const uploadedFile = fileUploadRef.current.files[0];
    if (uploadedFile) {
      const formData = new FormData();
      formData.append("file", uploadedFile);

      const fileReader = new FileReader();
      fileReader.onload = () => {
        setImageURL(fileReader.result);
        updateAdvertiseData('image', formData);
      };
      fileReader.readAsDataURL(uploadedFile);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateFields()) {
    saveAdvertiseData();
    }
  };

  const renderInputField = (field, handleChange) => (
    <div key={field.label}>
      <p>{field.label}</p>
      <input
        type="text"
        name={field.name}
        placeholder={field.placeholder}
        className="input-border"
        value={field.value}
        onChange={(e) => handleChange(e.target.name, e.target.value)}
      />
      {errors[field.name] && <span className={`error-message ${errors ? 'show' : ''}`}>{errors[field.name]}</span>}
    </div>
  );

  return (
    <section id="advertise" className="advertise">
      <div className="advertise-container">
        <div className="advertise-container-left">
          <div className="image-uploader">
            <label htmlFor="image">Upload Image*</label>
            <button type='submit' onClick={handleImageUpload} className="upload-button">
            <img
              src={imageURL}
              alt="Edit"
              className='upload-button-image'
            />
            </button>

            <form id="form" encType='multipart/form-data'>
              <input
                type="file"
                id="file"
                onChange={uploadImageDisplay}
                ref={fileUploadRef}
                hidden
              />
            </form>
          </div>
        </div>
        <div className="advertise-container-right">
          {advertiseFields.map(field => renderInputField(field, updateAdvertiseData))}
          <div>
            <label htmlFor="quantity">Quantity*</label>
            <input
              type="number"
              id="quantity"
              name="quantity"
              placeholder="Quantity"
              className="input-border"
              min="1"
              value={advertise.quantity}
              onChange={(e) => updateAdvertiseData('quantity', e.target.value)}
            />
          </div>

          <div className="listingStatus">
            <label htmlFor="expiryDate">Expiry Date*</label>
            <DatePicker
              className="input-border"
              id="expiryDate"
              selected={startDate}
              onChange={(date) => {
                setStartDate(date);
                updateAdvertiseData('expiryDate', date);
              }}
            />
          </div>

          <div className="listingStatus" id="listingStatus-select">
            <label htmlFor="listingStatus-select">Listing Status*</label>
            <select
              className='input-border'
              name="listingStatus"
              value={advertise.listingStatus}
              onChange={(e) => updateAdvertiseData('listingStatus', e.target.value)}
            >
              <option value="">Please choose an option</option>
              <option value="available">Available</option>
              <option value="claimed">Claimed</option>
              <option value="collected">Collected</option>
              <option value="expired">Expired</option>
            </select>
          </div>

          <div className='submit-profile-container'>
            <button className='btn' onClick={handleSubmit}>Submit</button>
          </div>
        </div>
      </div>
    </section>
  );
}

export default Advertise;
