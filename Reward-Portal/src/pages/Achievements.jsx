import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import Card from "../components/Card";
import { achievementsAPI } from "../services/api";
import { PlusCircle, Trash2 } from "lucide-react";

const Achievements = () => {
  const [achievements, setAchievements] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    description: "",
    points: 0,
    icon: "🏆",
  });

  useEffect(() => {
    loadAchievements();
  }, []);

  const loadAchievements = async () => {
    try {
      const res = await achievementsAPI.getAll();
      setAchievements(res.data);
    } catch (error) {
      console.error("Error loading achievements:", error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await achievementsAPI.create(formData);
      setFormData({ name: "", description: "", points: 0, icon: "🏆" });
      setShowForm(false);
      loadAchievements();
    } catch (error) {
      console.error("Error creating achievement:", error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("האם למחוק הישג זה?")) {
      try {
        await achievementsAPI.delete(id);
        loadAchievements();
      } catch (error) {
        console.error("Error deleting achievement:", error);
      }
    }
  };

  return (
    <Layout title="הישגים">
      <div className="mb-6">
        <button
          onClick={() => setShowForm(!showForm)}
          className="flex items-center gap-2 bg-primary text-white px-4 py-2 rounded-lg hover:bg-blue-700"
        >
          <PlusCircle size={20} />
          הישג חדש
        </button>
      </div>

      {showForm && (
        <Card className="p-6 mb-6">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-2">שם ההישג</label>
              <input
                type="text"
                value={formData.name}
                onChange={(e) =>
                  setFormData({ ...formData, name: e.target.value })
                }
                className="w-full px-4 py-2 border rounded-lg"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">תיאור</label>
              <textarea
                value={formData.description}
                onChange={(e) =>
                  setFormData({ ...formData, description: e.target.value })
                }
                className="w-full px-4 py-2 border rounded-lg"
                rows="3"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">נקודות</label>
              <input
                type="number"
                value={formData.points}
                onChange={(e) =>
                  setFormData({ ...formData, points: parseInt(e.target.value) })
                }
                className="w-full px-4 py-2 border rounded-lg"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">אייקון</label>
              <input
                type="text"
                value={formData.icon}
                onChange={(e) =>
                  setFormData({ ...formData, icon: e.target.value })
                }
                className="w-full px-4 py-2 border rounded-lg"
                placeholder="🏆"
              />
            </div>
            <div className="flex gap-2">
              <button
                type="submit"
                className="bg-primary text-white px-4 py-2 rounded-lg"
              >
                שמור
              </button>
              <button
                type="button"
                onClick={() => setShowForm(false)}
                className="bg-gray-300 px-4 py-2 rounded-lg"
              >
                ביטול
              </button>
            </div>
          </form>
        </Card>
      )}

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {achievements.map((achievement) => (
          <Card key={achievement._id} className="p-6">
            <div className="flex items-start justify-between">
              <div className="flex-1">
                <div className="text-4xl mb-3">{achievement.icon}</div>
                <h3 className="text-lg font-bold mb-2">{achievement.name}</h3>
                <p className="text-sm text-gray-600 mb-3">
                  {achievement.description}
                </p>
                <span className="bg-green-100 text-green-800 px-3 py-1 rounded-full text-sm font-semibold">
                  {achievement.points} נקודות
                </span>
              </div>
              <button
                onClick={() => handleDelete(achievement._id)}
                className="text-red-600 hover:text-red-800"
              >
                <Trash2 size={18} />
              </button>
            </div>
          </Card>
        ))}
      </div>
    </Layout>
  );
};

export default Achievements;
