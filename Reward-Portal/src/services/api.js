import axios from "axios";

const API_BASE_URL = "https://delighted-nanine-rewardx-76a4c86c.koyeb.app/api";

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export const usersAPI = {
  getAll: () => api.get("/users"),
  getById: (id) => api.get(`/users/${id}`),
  create: (data) => api.post("/users", data),
  delete: (id) => api.delete(`/users/${id}`),
  getAchievements: (id) => api.get(`/users/${id}/achievements`),
};

export const achievementsAPI = {
  getAll: () => api.get("/achievements"),
  getById: (id) => api.get(`/achievements/${id}`),
  create: (data) => api.post("/achievements", data),
  delete: (id) => api.delete(`/achievements/${id}`),
};

export const rulesAPI = {
  getAll: () => api.get("/rules"),
  create: (data) => api.post("/rules", data),
  delete: (id) => api.delete(`/rules/${id}`),
};

export const eventsAPI = {
  track: (data) => api.post("/events", data),
  getUserEvents: (userId) => api.get(`/events/${userId}`),
};

export default api;
