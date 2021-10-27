import React, {useEffect, useState} from 'react';
import BootstrapTable from 'react-bootstrap-table-next';
import axios from 'axios';
import {Builder, parseString} from 'xml2js';
import {second_host} from '../Utils/utils';
import {InfoModal} from './InfoModal';
import AddNewDiscipline from "./AddNewDiscipline";
import { useHistory } from "react-router-dom";

function DisciplineTable() {

    const xmlBuilder = new Builder();
    const [disciplines, setDisciplines] = useState([]);
    const [showError, setShowError] = useState(false)
    const [message, setMessage] = useState("")
    const history = useHistory();


    const columns = [
        { dataField: "id", text: "id" },
        { dataField: "name", text: "name" },
    ]

    const rowEvents = {
        onClick: (e, row, rowIndex) => {
            history.push(`discipline/${row.id}`);
        }
    };

    const catchInfo = (data) => {
        parseString(data, { explicitArray: false, ignoreAttrs: true }, function (err, result) {
          if (result && result.serverResponse && result.serverResponse.message) {
            setMessage(result.serverResponse.message)
          } else {
            setMessage("Unexpected error")
          }
          setShowError(true)
        })
      }

      const catchError = () => {
        setMessage("Unexpected error")
        setShowError(true)
      }

    const createNewDiscipline = async (object) => {
        let xmlObject = xmlBuilder.buildObject(object);
        axios.post(
            second_host, xmlObject, {headers: {'Content-Type': 'application/xml'}}
        ).then(data => {
            getDisciplineData();
        }).catch(function (error) {
            if (error.response) {
                // Request made and server responded
                if (error.response.data)
                    catchInfo(error.response.data)
                else
                    catchError()
            } else if (error.request) {
                // The request was made but no response was received
                catchError()
            } else {
                // Something happened in setting up the request that triggered an Error
                catchError()
            }
        })
    }


    const getDisciplineData = async () => {
        axios.get(
            second_host
        ).then (data => {
            parseString(data.data, { explicitArray: false, ignoreAttrs: true }, function (err, result) {
                switch (parseInt(result.disciplines_result.totalDisciplines)) {
                  case 0:
                    setDisciplines([]);
                    break;
                  case 1:
                    setDisciplines([result.disciplines_result.disciplines.discipline]);
                    break;
                  default:
                    setDisciplines(result.disciplines_result.disciplines.discipline)
                    break;
                }
              })
        })
        .catch(function (error) {
            if (error.response) {
              // Request made and server responded
              if (error.response.data)
                catchInfo(error.response.data)
              else
                catchError()
            } else if (error.request) {
              // The request was made but no response was received
              catchError()
            } else {
              // Something happened in setting up the request that triggered an Error
              catchError()
            }
          })
    }

    useEffect(() => {
        getDisciplineData();
    }, [])

    return (
        <div className="mt-5">
            <br/>
            <AddNewDiscipline createNewDiscipline={createNewDiscipline} />
            <InfoModal setShow={setShowError} show={showError} message={message} />
            <div style={{ overFlowX: 'auto' }} className="m-3">
                <BootstrapTable
                    classes="w-auto"
                    keyField="id"
                    data={disciplines}
                    columns={columns}
                    rowStyle={{ whiteSpace: 'nowrap', wordWrap: 'break-word' }}
                    rowEvents={ rowEvents }
                />
            </div>

        </div>
    )
}

export default DisciplineTable
