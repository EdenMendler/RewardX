import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import Card from "../components/Card";
import { rulesAPI, achievementsAPI } from "../services/api";
import { PlusCircle, Trash2 } from "lucide-react";

const Rules = () => {
  const [rules, setRules] = useState([]);
  const [achievements, setAchievements] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [formData, setFormData] = useState({
    achievement_id: "",
    event_type: "",
    condition_type: "count",
    threshold: 1,
    field: "",
  });

  useEffect(() => {
    loadRules();
    loadAchievements();
  }, []);

  const loadRules = async () => {
    try {
      const res = await rulesAPI.getAll();
      setRules(res.data);
    } catch (error) {
      console.error("Error loading rules:", error);
    }
  };

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
      await rulesAPI.create(formData);
      setFormData({
        achievement_id: "",
        event_type: "",
        condition_type: "count",
        threshold: 1,
        field: "",
      });
      setShowForm(false);
      loadRules();
    } catch (error) {
      console.error("Error creating rule:", error);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm("האם למחוק חוק זה?")) {
      try {
        await rulesAPI.delete(id);
        loadRules();
      } catch (error) {
        console.error("Error deleting rule:", error);
      }
    }
  };

  const getAchievementName = (id) => {
    const achievement = achievements.find((a) => a._id === id);
    return achievement ? achievement.name : id;
  };

  return (
    <Layout title="חוקים">
      <div className="mb-6">
        <button
          onClick={() => setShowForm(!showForm)}
          className="flex items-center gap-2 bg-primary text-white px-4 py-2 rounded-lg hover:bg-blue-700"
        >
          <PlusCircle size={20} />
          חוק חדש
        </button>
      </div>

      {showForm && (
        <Card className="p-6 mb-6">
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-sm font-medium mb-2">הישג</label>
              <select
                value={formData.achievement_id}
                onChange={(e) =>
                  setFormData({ ...formData, achievement_id: e.target.value })
                }
                className="w-full px-4 py-2 border rounded-lg"
                required
              >
                <option value="">בחר הישג</option>
                {achievements.map((a) => (
                  <option key={a._id} value={a._id}>
                    {a.name}
                  </option>
                ))}
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">
                סוג אירוע
              </label>
              <input
                type="text"
                value={formData.event_type}
                onChange={(e) =>
                  setFormData({ ...formData, event_type: e.target.value })
                }
                className="w-full px-4 py-2 border rounded-lg"
                placeholder="expense_added"
                required
              />
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">סוג תנאי</label>
              <select
                value={formData.condition_type}
                onChange={(e) =>
                  setFormData({ ...formData, condition_type: e.target.value })
                }
                className="w-full px-4 py-2 border rounded-lg"
              >
                <option value="count">ספירה</option>
                <option value="sum">סכום</option>
                <option value="unique_days">ימים ייחודיים</option>
              </select>
            </div>
            <div>
              <label className="block text-sm font-medium mb-2">סף</label>
              <input
                type="number"
                value={formData.threshold}
                onChange={(e) =>
                  setFormData({
                    ...formData,
                    threshold: parseInt(e.target.value),
                  })
                }
                className="w-full px-4 py-2 border rounded-lg"
                required
              />
            </div>
            {formData.condition_type === "sum" && (
              <div>
                <label className="block text-sm font-medium mb-2">שדה</label>
                <input
                  type="text"
                  value={formData.field}
                  onChange={(e) =>
                    setFormData({ ...formData, field: e.target.value })
                  }
                  className="w-full px-4 py-2 border rounded-lg"
                  placeholder="amount"
                />
              </div>
            )}
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

      <Card>
        <table className="w-full">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="px-6 py-3 text-right text-sm font-semibold">
                הישג
              </th>
              <th className="px-6 py-3 text-right text-sm font-semibold">
                סוג אירוע
              </th>
              <th className="px-6 py-3 text-right text-sm font-semibold">
                תנאי
              </th>
              <th className="px-6 py-3 text-right text-sm font-semibold">סף</th>
              <th className="px-6 py-3 text-right text-sm font-semibold">
                פעולות
              </th>
            </tr>
          </thead>
          <tbody>
            {rules.map((rule) => (
              <tr key={rule._id} className="border-b hover:bg-gray-50">
                <td className="px-6 py-4 font-medium">
                  {getAchievementName(rule.achievement_id)}
                </td>
                <td className="px-6 py-4">
                  <span className="bg-blue-100 text-blue-800 px-2 py-1 rounded text-sm">
                    {rule.event_type}
                  </span>
                </td>
                <td className="px-6 py-4">{rule.condition_type}</td>
                <td className="px-6 py-4">{rule.threshold}</td>
                <td className="px-6 py-4">
                  <button
                    onClick={() => handleDelete(rule._id)}
                    className="text-red-600 hover:text-red-800"
                  >
                    <Trash2 size={18} />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </Card>
    </Layout>
  );
};

export default Rules;
