import './App.css';
import Header from "./Components/Header";
import 'bootstrap/dist/css/bootstrap.min.css';
import 'react-bootstrap-table-next/dist/react-bootstrap-table2.min.css';
import 'react-bootstrap-table2-paginator/dist/react-bootstrap-table2-paginator.min.css';
import Table from "./Components/Table";

function App() {
  return (
    <div className="App">
      <Header/>
      <Table/>
    </div>
  );
}

export default App;
