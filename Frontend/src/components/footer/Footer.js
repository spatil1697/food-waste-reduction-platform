import React from 'react'
import './footer.css'

const footer = () => {
  return (
 <footer>
    <div className="top  footerbody">
      <div className="pages">
        <ul>
          <h3>Name</h3>
          <li><a href="#">Home</a></li>
          <li><a href="#">Catalog</a></li>
          <li><a href="#">Search</a></li>
          <li><a href="#">About</a></li>
          <li><a href="#">Contact</a></li>
        </ul>

        <ul>
          <h3>About Us</h3>
          <li><a href="#">Meet Our Team</a></li>
          <li><a href="#">Our Responsibilities</a></li>
          <li><a href="#">Our Codes</a></li>
          <li><a href="#">Our Values</a></li>
        </ul>
      </div>
      <div className="newsletter">
        <h3>Stay in Touch</h3>
        <form>
          <input
            type="email"
            name="newsletter_email"
            id="newsletter_email"
            placeholder="Email"
          />
          <input type="button" value="Submit" />
        </form>
      </div>
    </div>
  </footer>

  )
}

export default footer