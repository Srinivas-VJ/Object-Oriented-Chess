import Link from "next/link";
const NavItem = ({ text, href, active }) => {
  return (
    <Link href={href}>
        {text}
    </Link>
  );
};

export default NavItem;