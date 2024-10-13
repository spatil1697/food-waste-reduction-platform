import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Home from "./components/home_page/Home";
import Signup from "./components/signup/Signup";
import Login from "./components/Login/Login";
import Profile from "./components/profile/Profile";
import Root from './root'
import { AuthProvider } from "./context/AuthContext";
import { ProfileProvider } from './context/ProfileContext';
import { AdvertiseProvider } from './context/AdvertiseContext';
import MyProfile from './components/profile/MyProfile';
import DeleteAccount from './components/profile/DeleteAccount';
import MyFoodDonations from './components/food_donations/MyFoodDonations';
import Advertise from './components/advertise/Advertise'


const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <AuthProvider>
        <ProfileProvider> 
          <AdvertiseProvider>
            <Root />
          </AdvertiseProvider>
        </ProfileProvider>
      </AuthProvider>
    ),
    children: [
      {
        path: "/",
        element: <Home />,
      },
      {
        path: "/signup",
        element: <Signup/>,
      },
      {
        path: "/login",
        element: <Login/>,
      },
      {
        path: '/profile',
        element: <Profile />,
        children: [
          { path: '', element: <MyProfile /> },
          { path: 'delete-account', element: <DeleteAccount /> },
          { path: 'my-food-donations', element: <MyFoodDonations /> }
        ]
      },
      {
        path: "/advertise",
        element: <Advertise/>,
      },

    ],
  },
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);