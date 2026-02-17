# JavaScript web runner

This folder contains a browser-based JavaScript port of the `runSimpleEgo(...)` flow from `src/altr/BackEnd.java`.

## Run locally

From repository root:

```bash
python3 -m http.server 8080
```

Then open:

- http://localhost:8080/web/

## Features

- Form to configure experiment parameters.
- Run button to execute simulation in browser.
- Two graphs (result ratio + acceptance rate).
- CSV export of plotted results.
