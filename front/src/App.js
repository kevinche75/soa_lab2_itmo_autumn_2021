import './App.css';
import Header from "./Components/Header";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import LabWorkTable from "./Components/LabWorkTable";
import DisciplineTable from "./Components/DisciplineTable";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  Link
} from "react-router-dom";
import DisciplineLabWorksTable from "./Components/DisciplineLabWorksTable";

function App() {
  return (
    <div className="App">
      <Router>
        <Header />
        <Switch>
          <Route path="/discipline/:id">
            <DisciplineLabWorksTable/>
          </Route>
          <Route path="/discipline">
            <DisciplineTable />
          </Route>
          <Route path="/">
            <LabWorkTable />
          </Route>
        </Switch>
      </Router>
    </div>
  );
}

export default App;
