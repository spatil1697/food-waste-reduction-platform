import React from 'react';
import cover_Image from '../../assets/cover_image.jpg';
import "../../index.css";
import './home.css'

const Home = () => {
  return (
    <div>
      <section id='home' className='home'>
          <img src={cover_Image} alt="food" />
          <div className='header'>
            <h1> Cut the Waste,<br/>&nbsp; Feed the Future </h1> 
          </div>
      </section>
      <div className='description full-width-background '>
          <p className='header_paragraph'>*****</p>
          <p className='header_paragraph'>"Cut the Waste is your solution to reducing food waste effortlessly.  With easy inventory management
             timely reminders, and direct connections to donate excess food, it helps you save food and make a 
             difference. Join us in our mission to ensure no meal goes to waste"</p>
          <p className='header_paragraph'>*****</p>
      </div>
    </div>

  );
};

export default Home;