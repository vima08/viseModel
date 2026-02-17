class Person {
  static uniqueId = 0;

  constructor(money) {
    this.id = Person.uniqueId++;
    this.initialMoney = money;
    this.money = money;
  }

  reset() {
    this.money = this.initialMoney;
  }
}

function randomGaussian() {
  let u = 0;
  let v = 0;
  while (u === 0) u = Math.random();
  while (v === 0) v = Math.random();
  return Math.sqrt(-2.0 * Math.log(u)) * Math.cos(2 * Math.PI * v);
}

function drawOffer(distribution, mean, deviation, k) {
  if (distribution === 'NormalDistribution') {
    return mean + deviation * randomGaussian();
  }

  if (distribution === 'ContinuousUniformDistribution') {
    const a = mean - (deviation * Math.sqrt(12)) / 2;
    const b = mean + (deviation * Math.sqrt(12)) / 2;
    return a + (b - a) * Math.random();
  }

  const b = mean + deviation * Math.sqrt(((k - 1) * (k - 2)) / 2);
  const a = mean - b;
  const uniform = Math.random();

  if (uniform <= 0.5) {
    return b + a * (2 - Math.pow(2 * uniform, -1 / k));
  }
  return b + a * Math.pow(2 - 2 * uniform, -1 / k);
}

function runSimpleEgo(params) {
  const {
    capital,
    distribution,
    mu,
    sigma,
    k,
    iterNum,
    stepNum,
    peopleCount,
    alpha,
    start,
    finish,
    step
  } = params;

  const xValues = [];
  const ratioValues = [];
  const acceptanceValues = [];

  for (let i = start; i <= finish; i += step) {
    const mean = mu * i;
    const people = Array.from({ length: peopleCount }, () => new Person(capital));

    let totalIncrement = 0;
    let incrementCounter = 0;
    let acceptedRounds = 0;
    const totalRounds = iterNum * stepNum;

    for (let iter = 0; iter < iterNum; iter += 1) {
      for (let st = 0; st < stepNum; st += 1) {
        const offers = people.map((person) => ({
          personId: person.id,
          amount: drawOffer(distribution, mean, sigma, k)
        }));

        let votes = 0;
        for (const offer of offers) {
          if (offer.amount > 0) votes += 1;
        }

        const accepted = votes / people.length > alpha;

        if (!accepted) {
          incrementCounter += people.length;
        } else {
          acceptedRounds += 1;
          for (const offer of offers) {
            const person = people.find((p) => p.id === offer.personId);
            person.money += offer.amount;
            totalIncrement += offer.amount;
            incrementCounter += 1;
          }
        }

        for (let idx = people.length - 1; idx >= 0; idx -= 1) {
          if (people[idx].money < 0) {
            people.splice(idx, 1);
          }
        }

        if (people.length === 0) break;
      }

      for (const person of people) {
        person.reset();
      }
    }

    xValues.push(mean);
    ratioValues.push(incrementCounter === 0 ? 0 : totalIncrement / incrementCounter);
    acceptanceValues.push(totalRounds === 0 ? 0 : acceptedRounds / totalRounds);
  }

  return { xValues, ratioValues, acceptanceValues };
}

const form = document.getElementById('experiment-form');
const resultJson = document.getElementById('result-json');
const exportBtn = document.getElementById('export-btn');

const resultCtx = document.getElementById('result-chart');
const acceptanceCtx = document.getElementById('acceptance-chart');

let lastRun = null;

const resultChart = new Chart(resultCtx, {
  type: 'line',
  data: {
    labels: [],
    datasets: [{
      label: 'result = totalIncrement / incrementCounter',
      data: [],
      borderColor: '#2563eb',
      borderWidth: 2,
      fill: false
    }]
  },
  options: {
    responsive: true,
    scales: {
      x: { title: { display: true, text: 'mean offer (mu * multiplier)' } },
      y: { title: { display: true, text: 'result ratio' } }
    }
  }
});

const acceptanceChart = new Chart(acceptanceCtx, {
  type: 'line',
  data: {
    labels: [],
    datasets: [{
      label: 'acceptance rate',
      data: [],
      borderColor: '#059669',
      borderWidth: 2,
      fill: false
    }]
  },
  options: {
    responsive: true,
    scales: {
      x: { title: { display: true, text: 'mean offer (mu * multiplier)' } },
      y: { min: 0, max: 1, title: { display: true, text: 'accepted rounds fraction' } }
    }
  }
});

form.addEventListener('submit', (event) => {
  event.preventDefault();

  const data = new FormData(form);
  const params = {
    distribution: data.get('distribution'),
    capital: Number(data.get('capital')),
    mu: Number(data.get('mu')),
    sigma: Number(data.get('sigma')),
    k: Number(data.get('k')),
    iterNum: Number(data.get('iterNum')),
    stepNum: Number(data.get('stepNum')),
    peopleCount: Number(data.get('peopleCount')),
    alpha: Number(data.get('alpha')),
    start: Number(data.get('start')),
    finish: Number(data.get('finish')),
    step: Number(data.get('step'))
  };

  const result = runSimpleEgo(params);
  lastRun = { params, ...result };
  exportBtn.disabled = false;

  resultChart.data.labels = result.xValues;
  resultChart.data.datasets[0].data = result.ratioValues;
  resultChart.update();

  acceptanceChart.data.labels = result.xValues;
  acceptanceChart.data.datasets[0].data = result.acceptanceValues;
  acceptanceChart.update();

  resultJson.textContent = JSON.stringify(lastRun, null, 2);
});

function toCsv(run) {
  const header = [
    'x_mean_offer',
    'result_total_increment_over_increment_counter',
    'acceptance_rate'
  ];

  const rows = run.xValues.map((x, idx) => [
    x,
    run.ratioValues[idx],
    run.acceptanceValues[idx]
  ]);

  return [header, ...rows].map((row) => row.join(',')).join('\n');
}

exportBtn.addEventListener('click', () => {
  if (!lastRun) return;

  const blob = new Blob([toCsv(lastRun)], { type: 'text/csv;charset=utf-8;' });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = `vise-model-${Date.now()}.csv`;
  link.click();
  URL.revokeObjectURL(url);
});
