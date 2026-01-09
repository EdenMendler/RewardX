import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import Card from "../components/Card";
import { usersAPI, achievementsAPI, rulesAPI } from "../services/api";
import { Users, Trophy, Target, TrendingUp } from "lucide-react";

const Dashboard = () => {
  const [stats, setStats] = useState({
    users: 0,
    achievements: 0,
    rules: 0,
    totalPoints: 0,
  });

  useEffect(() => {
    loadStats();
  }, []);

  const loadStats = async () => {
    try {
      const [usersRes, achievementsRes, rulesRes] = await Promise.all([
        usersAPI.getAll(),
        achievementsAPI.getAll(),
        rulesAPI.getAll(),
      ]);

      const totalPoints = usersRes.data.reduce(
        (sum, user) => sum + (user.total_points || 0),
        0
      );

      setStats({
        users: usersRes.data.length,
        achievements: achievementsRes.data.length,
        rules: rulesRes.data.length,
        totalPoints,
      });
    } catch (error) {
      console.error("Error loading stats:", error);
    }
  };

  const statCards = [
    {
      icon: Users,
      label: "משתמשים",
      value: stats.users,
      color: "text-blue-600",
    },
    {
      icon: Trophy,
      label: "הישגים",
      value: stats.achievements,
      color: "text-yellow-600",
    },
    {
      icon: Target,
      label: "חוקים",
      value: stats.rules,
      color: "text-green-600",
    },
    {
      icon: TrendingUp,
      label: "נקודות כוללות",
      value: stats.totalPoints,
      color: "text-purple-600",
    },
  ];

  return (
    <Layout title="דשבורד">
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {statCards.map(({ icon: Icon, label, value, color }) => (
          <Card key={label} className="p-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-gray-600">{label}</p>
                <p className="text-3xl font-bold mt-2">{value}</p>
              </div>
              <Icon size={40} className={color} />
            </div>
          </Card>
        ))}
      </div>
    </Layout>
  );
};

export default Dashboard;
