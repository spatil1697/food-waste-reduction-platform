import { createContext, useState, useEffect } from "react";
import { jwtDecode } from "jwt-decode";
import { useNavigate } from 'react-router-dom';


const AuthContext = createContext();

export default AuthContext;

export const AuthProvider = ({ children }) => {
  const [authTokens, setAuthTokens] = useState(() =>
    localStorage.getItem("authTokens")
      ? JSON.parse(localStorage.getItem("authTokens"))
      : null
  );
  const [user, setUser] = useState(() =>
    localStorage.getItem("authTokens")
      ? jwtDecode(localStorage.getItem("authTokens"))
      : null
  );
  const [loading, setLoading] = useState(true);

  const navigate = useNavigate();

  // If the user is present in the database (credentials are valid), 
  // the user is logged in. 
  const loginUser = async (email, password) => {
    const response = await fetch("http://localhost:8080/user/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({
        email,
        password,
      })
    });
    const data = await response.json();

    if (response.status === 200) {
      setAuthTokens(data); // Assuming the JWT token is stored in data.jwt
      setUser(jwtDecode(data.jwt)); // Decoding the JWT token
      localStorage.setItem("authTokens", JSON.stringify(data));
      navigate("/");
    } else {
      alert("You have entered an invalid username or password!");
    }
  };
  
  
  // This function registers the user in the database. 
  const registerUser = async (firstName, lastName, email, password, userType) => {
    try {
      const response = await fetch("http://localhost:8080/user/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          firstName,
          lastName,
          email,
          password,
          userType,
        })
      });
      return response;
    } catch (error) {
      console.error("Error during signup:", error);
      throw new Error("An error occurred. Please try again.");
    }
  };
  

  // logs the user out & clears the local storage.
  const logoutUser = () => {
    setAuthTokens(null);
    setUser(null);
    localStorage.removeItem("authTokens");
    navigate("/");
  };

  
  const contextData = {
    user,
    setUser,
    authTokens,
    setAuthTokens,
    registerUser,
    loginUser,
    logoutUser
  };


  useEffect(() => {
    if (authTokens) {
      const decodedUser = jwtDecode(authTokens.jwt); // Accessing the JWT token
      setUser(decodedUser);
    }
    setLoading(false);
  }, [authTokens]);

  return (
    <AuthContext.Provider value={contextData}>
      {loading ? null : children}
    </AuthContext.Provider>
  );
};