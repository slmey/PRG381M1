<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Wellness Dashboard</title>
<link rel="stylesheet" href="style.css" />
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" integrity="sha512-Fo3rlrZj/k7ujTnHg4CGR2D7kSs0v4LLanw2qksYuRlEzO+tcaEPQogQ0KaoGN26/zrn20ImR1DfuLWnOo7aBA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<div class="welcome-header">
<h2>Welcome, <%= session.getAttribute("studentName") != null ? session.getAttribute("studentName") : "User" %></h2>
</div>
<section class="dashboard-section">
<div class="dashboard-container">
<div class="dashboard-card">
<i class="fas fa-heartbeat icon"></i>
<h3>Heart Rate</h3>
<p>80 Bpm</p>
</div>
<div class="dashboard-card">
<i class="fas fa-bed icon"></i>
<h3>Sleep</h3>
<p>8 Hours <span class="highlight">+0%</span></p>
</div>
<div class="dashboard-card">
<i class="fas fa-tint icon"></i>
<h3>Water Intake</h3>
<p>2.1 Litres <span class="highlight">+40%</span></p>
</div>
<div class="dashboard-card">
<i class="fas fa-fire icon"></i>
<h3>Calories</h3>
<p>1486 Kcal</p>
</div>
<div class="dashboard-card">
<i class="fas fa-dumbbell icon"></i>
<h3>Today Challenges</h3>
<ul>
<li>15,000 Steps</li>
<li>Drink 10 glasses of water</li>
</ul>
</div>
<div class="dashboard-card">
<i class="fas fa-utensils icon"></i>
<h3>Recommended Food</h3>
<p>Almonds - 150 Cal</p>
<p>Banana - 100 Cal</p>
<p>Cashews - 200 Cal</p>
</div>
<div class="dashboard-card">
<i class="fas fa-running icon"></i>
<h3>Activity Tracking</h3>
<p>Steps: 10,400</p>
<p>Distance: 8.5 Km</p>
<p>Time: 47 min</p>
<p>Speed: 3.5 Km/h</p>
</div>
<div class="chart-container">
<i class="fas fa-chart-bar icon"></i>
<h3>Daily Steps</h3>
<canvas id="stepsChart"></canvas>
<script>
                        const ctx = document.getElementById('stepsChart').getContext('2d');
                        const stepsChart = new Chart(ctx, {
                            type: 'bar',
                            data: {
                                labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
                                datasets: [{
                                    label: 'Steps',
                                    data: [8000, 9500, 10400, 12000, 9000, 11000, 13000],
                                    backgroundColor: 'rgba(0, 150, 255, 0.8)',
                                    borderColor: 'rgba(0, 150, 255, 1)',
                                    borderWidth: 1
                                }]
                            },
                            options: {
                                scales: {
                                    y: {
                                        beginAtZero: true,
                                        ticks: { color: '#fff' }
                                    },
                                    x: { ticks: { color: '#fff' } }
                                },
                                plugins: {
                                    legend: { labels: { color: '#fff' } }
                                }
                            }
                        });
</script>
</div>
</div>
</section>
<div class="logout-footer">
<form action="${pageContext.request.contextPath}/LogoutServlet" method="post">
<button id="logout-btn" type="submit">Log Out</button>
</form>
</div>
</body>
</html>