import React, {useEffect, useState} from 'react'
import {Builder, parseString} from "xml2js";
import {useParams} from "react-router";
import {InfoModal} from "./InfoModal";
import BootstrapTable from "react-bootstrap-table-next";
import axios from "axios";
import {constructQueryParams, first_host, second_host} from "../Utils/utils";

const DisciplineLabWorksTable = () => {

    const xmlBuilder = new Builder();
    const [labs, setLabs] = useState([]);
    const [showError, setShowError] = useState(false)
    const [message, setMessage] = useState("")
    const [name, setName] = useState("")
    const { id } = useParams()

    const handleDelete = async (labWorkId) => {
        axios.delete(
            `${second_host}/${id}/labwork/${labWorkId}/remove`
        ).then(data => {
            getLabsData();
        })
            .catch(function (error) {
                baseCatch(error)
            })
    }


    const columns = [
        { dataField: "id", text: "id" },
        { dataField: "name", text: "name" },
        {
            dataField: "remove",
            text: "Delete",
            formatter: (cellContent, row) => {
                return (
                    <button
                        className="btn btn-danger btn-xs"
                        onClick={() => handleDelete(row.id)}
                    >
                        Delete
                    </button>
                );
            },
        },
    ]

    const baseCatch = (error) => {
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
    }

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

    const searchLab = async () => {
        axios.get(
            `${second_host}/${id}`
        )
            .then(data => {
                parseString(data.data, { explicitArray: false, ignoreAttrs: true }, function (err, result) {
                    setName(result.discipline.name)
                })
                getLabsData()
            })
            .catch(function (error) {
                baseCatch(error)
            })
    }

    const getLabsData = async () => {
        return axios.get(
            `${second_host}/${id}/labworks`
        ).then(data => {
            parseString(data.data, { explicitArray: false, ignoreAttrs: true }, function (err, result) {
                switch (parseInt(result.labworks_result.totalLabWorks)) {
                    case 0:
                        setLabs([]);
                        break;
                    case 1:
                        setLabs([result.labworks_result.labworks.labwork]);
                        break;
                    default:
                        setLabs(result.labworks_result.labworks.labwork)
                        break;
                }
            })
        })
            .catch(function (error) {
                baseCatch(error)
            })
    }

    useEffect(() => {
        searchLab();
    }, [])

    return (
        <div>
            <br/>
            <InfoModal setShow={setShowError} show={showError} message={message} />
            <h2>{name}</h2>
            <br/>
            <div style={{ overFlowX: 'auto' }} className="m-3">
                <BootstrapTable
                    classes="w-auto"
                    keyField="id"
                    data={labs}
                    columns={columns}
                    rowStyle={{ whiteSpace: 'nowrap', wordWrap: 'break-word' }}
                />
            </div>
        </div>
    )
}

export default DisciplineLabWorksTable
