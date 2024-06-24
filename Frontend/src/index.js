import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Home from "./components/home_page/Home";
import Signup from "./components/signup/Signup";
import Login from "./components/Login/Login";
import Profile from "./components/Profile/Profile";
import Root from './root'
import { AuthProvider } from "./context/AuthContext";
import { ProfileProvider } from './context/ProfileContext';
import MyProfile from './components/Profile/MyProfile';
import DeleteAccount from './components/Profile/DeleteAccount';
import MyFoodDonations from './components/food_donations/MyFoodDonations';


const router = createBrowserRouter([
  {
    path: "/",
    element: (
      <AuthProvider>
        <ProfileProvider> 
          <Root />
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
      }

    ],
  },
]);

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <RouterProvider router={router} />
  </React.StrictMode>
);