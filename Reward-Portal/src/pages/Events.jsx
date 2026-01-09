import React, { useState, useEffect } from "react";
import Layout from "../components/Layout";
import Card from "../components/Card";
import { eventsAPI, usersAPI } from "../services/api";
import { Activity } from "lucide-react";

const Events = () => {
  const [users, setUsers] = useState([]);
  const [selectedUser, setSelectedUser] = useState("");
  const [formData, setFormData] = useState({
    event_type: "",
    event_data: "",
  });

  useEffect(() => {
    loadUsers();
  }, []);

  const loadUsers = async () => {
    try {
      const res = await usersAPI.getAll();
      setUsers(res.data);
    } catch (error) {
      console.error("Error loading users:", error);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      let eventData = {};
      try {
        eventData = JSON.parse(formData.event_data);
      } catch {
        eventData = { value: formData.event_data };
      }

      const res = await eventsAPI.track({
        user_id: selectedUser,
        event_type: formData.event_type,
        event_data: eventData,
      });

      if (res.data.new_achievements.length > 0) {
        alert(
          `הישג חדש! ${res.data.new_achievements.map((a) => a.name).join(", ")}`
        );
      } else {
        alert("האירוע נשמר בהצלחה");
      }

      setFormData({ event_type: "", event_data: "" });
    } catch (error) {
      console.error("Error tracking event:", error);
      alert("שגיאה בשמירת האירוע");
    }
  };

  return (
    <Layout title="אירועים">
      <Card className="p-6">
        <div className="flex items-center gap-2 mb-6">
          <Activity className="text-primary" size={24} />
          <h2 className="text-xl font-bold">מעקב אירוע חדש</h2>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-2">משתמש</label>
            <select
              value={selectedUser}
              onChange={(e) => setSelectedUser(e.target.value)}
              className="w-full px-4 py-2 border rounded-lg"
              required
            >
              <option value="">בחר משתמש</option>
              {users.map((user) => (
                <option key={user._id} value={user._id}>
                  {user.username} ({user.email})
                </option>
              ))}
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium mb-2">סוג אירוע</label>
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
            <label className="block text-sm font-medium mb-2">
              מידע נוסף (JSON או טקסט)
            </label>
            <textarea
              value={formData.event_data}
              onChange={(e) =>
                setFormData({ ...formData, event_data: e.target.value })
              }
              className="w-full px-4 py-2 border rounded-lg font-mono text-sm"
              rows="4"
              placeholder='{"amount": 100, "category": "food"}'
              required
            />
            <p className="text-xs text-gray-500 mt-1">
              ניתן להזין JSON או טקסט רגיל
            </p>
          </div>

          <button
            type="submit"
            className="bg-primary text-white px-6 py-2 rounded-lg hover:bg-blue-700"
          >
            שלח אירוע
          </button>
        </form>
      </Card>
    </Layout>
  );
};

export default Events;
