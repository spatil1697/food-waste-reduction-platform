import React, { useState, useRef, useEffect } from 'react';
import { Link, useLocation } from 'react-router-dom';
import { RxHamburgerMenu } from 'react-icons/rx';
import { FaBowlFood } from 'react-icons/fa6';
import { CgProfile } from "react-icons/cg";
import { FaAngleDown } from "react-icons/fa";
import './navbar.css';
import { useContext } from "react";
import AuthContext from "../../context/AuthContext";

const Navbar = () => {
  const [showNavbar, setShowNavbar] = useState(false);
  const [showDropdown, setShowDropdown] = useState(false);
  const dropdownTimeoutRef = useRef(null);
  const { user, logoutUser } = useContext(AuthContext);
  const location = useLocation();

  const handleShowNavbar = () => {
    setShowNavbar(!showNavbar);
  };

  const handleMouseEnter = () => {
    if (dropdownTimeoutRef.current) {
      clearTimeout(dropdownTimeoutRef.current);
    }
    setShowDropdown(true);
  };

  const handleMouseLeave = () => {
    dropdownTimeoutRef.current = setTimeout(() => {
      setShowDropdown(false);
    }, 400);
  };

  const toggleDropdown = () => {
    setShowDropdown(!showDropdown);
  };

  const handleLinkClick = () => {
    setShowNavbar(false);
  };

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > 100) {
        setShowNavbar(false);
        document.body.classList.remove('no-scroll');
      }
    };

    const handleScrollLock = () => {
      if (showNavbar) {
        document.body.classList.add('no-scroll');
      } else {
        document.body.classList.remove('no-scroll');
      }
    };

    handleScrollLock();
    window.addEventListener('resize', handleResize);
    window.addEventListener('resize', handleScrollLock);

    return () => {
      window.removeEventListener('resize', handleResize);
      window.removeEventListener('resize', handleScrollLock);
    };
  }, [showNavbar]);

  return (
    <nav className="navbar">
      <div className="container">
        <div className="logo">
          <Link to="/" className="logo">
            <FaBowlFood color='black' size="30px" />
          </Link>
        </div>
        <div className="menu-icon" onClick={handleShowNavbar}>
          <RxHamburgerMenu color='black' size="30px" />
        </div>
        <div className={`nav-elements ${showNavbar ? 'active' : ''}`}>
          <ul>
            <li className={location.pathname === '/' ? 'active' : ''} onClick={handleLinkClick}>
              <Link to="/">Home</Link>
            </li>
            <li className={location.pathname === '/projects' ? 'active' : ''} onClick={handleLinkClick}>
              <Link to="/projects">Donations</Link>
            </li>
            <li className={location.pathname === '/about' ? 'active' : ''} onClick={handleLinkClick}>
              <Link to="/about">Recipes</Link>
            </li>
            <li onMouseEnter={handleMouseEnter} onMouseLeave={handleMouseLeave} onClick={toggleDropdown} className={showDropdown ? 'active' : ''}>
              <span className="profile-icon"><CgProfile size="25px" /></span>
              <span className="profile-text">Profile <FaAngleDown size="12px" /></span>
              {showDropdown && (
                <div className="dropdown-menu">
                  {user ? (
                    <>
                      <span className="profile-name">Hello {user.firstName}!</span>
                      <Link to='/profile' onClick={handleLinkClick}>My Profile</Link>
                    </>
                  ) : (
                    <Link to="/Login" onClick={handleLinkClick}>Signup/Login</Link>
                  )}
                  <Link to="/my-food" onClick={handleLinkClick}>My Food</Link>
                  <Link to="/shopping-list" onClick={handleLinkClick}>Shopping List</Link>
                  <Link to="/food-requests" onClick={handleLinkClick}>Food Requests</Link>
                  <Link to="/food-donations" onClick={handleLinkClick}>Food Donations</Link>
                  {user ? (
                    <>
                      <Link onClick={() => { logoutUser(); handleLinkClick(); }}>Logout</Link>
                    </>
                  ) : null}
                </div>
              )}
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
