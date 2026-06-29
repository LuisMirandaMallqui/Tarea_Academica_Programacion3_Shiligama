window.chartHelper = {
  charts: {},

  renderSalesChart: function (canvasId, labels, data) {
    // Destroy previous chart instance if it exists to avoid overlapping
    if (this.charts[canvasId]) {
      this.charts[canvasId].destroy();
    }

    const canvas = document.getElementById(canvasId);
    if (!canvas) return;

    const ctx = canvas.getContext('2d');
    
    // Forest green theme colors
    const primaryColor = '#1A6B3C';
    
    this.charts[canvasId] = new Chart(ctx, {
      type: 'line',
      data: {
        labels: labels,
        datasets: [{
          label: 'Ventas',
          data: data,
          borderColor: primaryColor,
          backgroundColor: 'rgba(26, 107, 60, 0.08)',
          borderWidth: 3,
          pointBackgroundColor: primaryColor,
          pointBorderColor: '#ffffff',
          pointBorderWidth: 2,
          pointRadius: 5,
          pointHoverRadius: 7,
          tension: 0.35,
          fill: true
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          },
          tooltip: {
            backgroundColor: '#1e293b',
            titleColor: '#ffffff',
            bodyColor: '#ffffff',
            padding: 10,
            borderRadius: 6,
            displayColors: false,
            callbacks: {
              label: function (context) {
                return `S/. ${context.parsed.y.toFixed(2)}`;
              }
            }
          }
        },
        scales: {
          y: {
            grid: {
              color: 'rgba(148, 163, 184, 0.1)',
            },
            border: {
              dash: [4, 4]
            },
            ticks: {
              font: {
                size: 11
              },
              callback: function (value) {
                return 'S/. ' + value;
              }
            }
          },
          x: {
            grid: {
              display: false
            },
            ticks: {
              font: {
                size: 11
              }
            }
          }
        }
      }
    });
  },

  renderOrdersComparisonChart: function (canvasId, labels, presencialData, onlineData) {
    if (this.charts[canvasId]) {
      this.charts[canvasId].destroy();
    }

    const canvas = document.getElementById(canvasId);
    if (!canvas) return;

    const ctx = canvas.getContext('2d');

    this.charts[canvasId] = new Chart(ctx, {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [
          {
            label: 'Presencial',
            data: presencialData,
            backgroundColor: '#1A6B3C',
            borderRadius: 4,
            borderSkipped: false
          },
          {
            label: 'Online',
            data: onlineData,
            backgroundColor: '#22c55e',
            borderRadius: 4,
            borderSkipped: false
          }
        ]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: 'top',
            labels: {
              boxWidth: 12,
              font: {
                size: 11
              }
            }
          },
          tooltip: {
            backgroundColor: '#1e293b',
            titleColor: '#ffffff',
            bodyColor: '#ffffff',
            padding: 10,
            borderRadius: 6
          }
        },
        scales: {
          y: {
            grid: {
              color: 'rgba(148, 163, 184, 0.1)',
            },
            border: {
              dash: [4, 4]
            },
            ticks: {
              font: {
                size: 11
              }
            }
          },
          x: {
            grid: {
              display: false
            },
            ticks: {
              font: {
                size: 11
              }
            }
          }
        }
      }
    });
  },

  // Alias used by Admin Dashboard
  renderChannelsChart: function (canvasId, labels, presencialData, onlineData) {
    this.renderOrdersComparisonChart(canvasId, labels, presencialData, onlineData);
  }
};

