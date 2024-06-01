import React, { useState, useRef } from 'react';
import { Link } from 'react-router-dom';
import { RxHamburgerMenu } from 'react-icons/rx';
import { FaBowlFood } from 'react-icons/fa6';
import { CgProfile } from "react-icons/cg";
import { FaAngleDown } from "react-icons/fa";
import './navbar.css';

const Navbar = () => {
    const [showNavbar, setShowNavbar] = useState(false);
    const [showDropdown, setShowDropdown] = useState(false);
    const dropdownTimeoutRef = useRef(null);

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
                <div className={`nav-elements ${showNavbar && 'active'}`}>
                    <ul>
                        <li>
                            <Link to="/">Home</Link>
                        </li>
                        <li>
                            <Link to="/projects">Donations</Link>
                        </li>
                        <li>
                            <Link to="/about">Recipes</Link>
                        </li>
                        <li
                            onMouseEnter={handleMouseEnter}
                            onMouseLeave={handleMouseLeave}
                            onClick={toggleDropdown}
                        >
                            <span className="profile-icon"><CgProfile size="25px" /></span>
                            <span className="profile-text">Profile <FaAngleDown size="12px" /></span>
                            {showDropdown && (
                                <div className="dropdown-menu">
                                    <Link to="/Signup">Signup/Login</Link>
                                    <Link to="/my-food">My Food</Link>
                                    <Link to="/shopping-list">Shopping List</Link>
                                    <Link to="/food-requests">Food Requests</Link>
                                    <Link to="/food-donations">Food Donations</Link>
                                </div>
                            )}
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    )
}

export default Navbar;