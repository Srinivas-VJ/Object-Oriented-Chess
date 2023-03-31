import Link from "next/link";
import Image from "next/image";
import React, { useEffect, useState } from "react";
import NavItem from "./NavItem";
import { getAuthToken, getUserDetails } from "../utils/authenticate";

const Navbar = () => {
  
  var user = getUserDetails();

  const [navActive, setNavActive] = useState(null);
  const [activeIdx, setActiveIdx] = useState(-1);
  const [mainMenu, setMenu] = useState([
    { text: "Home", href: "/" },
    { text: "About Us", href: "/about" },
    { text: "Login/Signup", href: "/login"}
  ])

  useEffect( () => {
    if (user != null) {
      const newMenu = mainMenu;
      newMenu[2]["text"] = user.username;
      newMenu[2]["href"] = "/users/" + user.username;
      setMenu(newMenu)
    }
  }, [])

  return (
    <header>
      <nav className={`nav`} style = {{background : "rgb(193 137 81)"}}>
        <Link href={"/"}>

            <h1 className="logo">Chess</h1>
        </Link>
        <div
          onClick={() => setNavActive(!navActive)}
          className={`nav__menu-bar`}
        >
          <div></div>
          <div></div>
          <div></div>
        </div>
        <div className={`${navActive ? "active" : ""} nav__menu-list`} style = {{background : "rgb(193 137 81)"}}>
          {mainMenu.map((menu, idx) => (
            <div
              onClick={() => {
                setActiveIdx(idx);
                setNavActive(false);
              }}
              key={menu.text}
            >
              <NavItem active={activeIdx === idx} {...menu} />
            </div>
          ))}
        </div>
      </nav>
    </header>
  );
};

export default Navbar;