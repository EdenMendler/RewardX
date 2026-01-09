import React from "react";
import { NavLink } from "react-router-dom";
import { LayoutDashboard, Users, Trophy, Target, Activity } from "lucide-react";

const Sidebar = () => {
  const links = [
    { to: "/", icon: LayoutDashboard, label: "דשבורד" },
    { to: "/users", icon: Users, label: "משתמשים" },
    { to: "/achievements", icon: Trophy, label: "הישגים" },
    { to: "/rules", icon: Target, label: "חוקים" },
    { to: "/events", icon: Activity, label: "אירועים" },
  ];

  return (
    <div className="w-64 bg-gray-900 text-white h-screen fixed right-0 top-0">
      <div className="p-6">
        <h1 className="text-2xl font-bold text-primary">RewardX</h1>
        <p className="text-sm text-gray-400 mt-1">פורטל ניהול</p>
      </div>

      <nav className="mt-6">
        {links.map(({ to, icon: Icon, label }) => (
          <NavLink
            key={to}
            to={to}
            className={({ isActive }) =>
              `flex items-center gap-3 px-6 py-3 transition-colors ${
                isActive
                  ? "bg-primary text-white border-r-4 border-white"
                  : "text-gray-300 hover:bg-gray-800"
              }`
            }
          >
            <Icon size={20} />
            <span>{label}</span>
          </NavLink>
        ))}
      </nav>
    </div>
  );
};

export default Sidebar;
