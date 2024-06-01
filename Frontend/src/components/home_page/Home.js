import React from 'react';
import cover_Image from '../../assets/cover_image.jpg';
import "../../index.css";
import './home.css'

const Home = () => {
  return (
    <section id='home' className='home'> 
        <img src={cover_Image} alt="food" />
        <div className='header'>
            <h1> Cut the Waste,<br/>&nbsp; Feed the Future </h1> 
        </div>
    </section>
  );
};

export default Home;