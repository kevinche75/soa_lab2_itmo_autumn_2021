import React, {useCallback, useEffect, useState} from 'react';
import BootstrapTable from 'react-bootstrap-table-next';
import cellEditFactory, {Type} from 'react-bootstrap-table2-editor';
import filterFactory, {
  Comparator,
  dateFilter,
  numberFilter,
  selectFilter,
  textFilter
} from 'react-bootstrap-table2-filter';
import paginationFactory from 'react-bootstrap-table2-paginator';
import axios from 'axios';
import {Builder, parseString} from 'xml2js';
import AddNewLab from './AddNewLab';
import moment from 'moment';
import {
  constructQueryParams,
  constructSortField,
  constructUpdateObject,
  defaultSorted,
  first_host,
  options, second_host,
  sizePerPageRenderer, third_host
} from '../Utils/utils';
import SpecialFuncs from './SpecialFuncs';
import {InfoModal} from './InfoModal';
import LabModal from './LabModal';
import {Button, Dropdown, DropdownButton, FormControl, InputGroup} from "react-bootstrap";

const LabWorkTable = () => {

  const xmlBuilder = new Builder();
  const [labs, setLabs] = useState([]);
  const [activePage, setActivePage] = useState(1);
  const [activeSizePerPage, setActiveSizePerPage] = useState(10)
  const [totalSize, setTotalSize] = useState(0)
  const [activeSortField, setActiveSortField] = useState("")
  const [firstQuery, setFirstQuery] = useState(false)
  const [showError, setShowError] = useState(false)
  const [message, setMessage] = useState("")
  const [disciplines, setDisciplines] = useState([]);
  const [lab, setLab] = useState({
    name: "",
    creationDate: "",
    minimalPoint: "",
    maximumPoint: "",
    personalQualitiesMaximum: "",
    difficulty: "",
    coordinates: {
      x: "",
      y: ""
    },
    author: {
      name: "",
      weight: "",
      location: {
        x: "",
        y: "",
        z: "",
        name: ""
      }
    }
  })
  const [showLab, setShowLab] = useState(false)
  const [filterFields, setFilterFields] = useState({
    name: '',
    creationDate: '',
    minimalPoint: '',
    maximumPoint: '',
    personalQualitiesMaximum: '',
    difficulty: '',
    "coordinates.x": '',
    "coordinates.y": '',
    "author.name": '',
    "author.weight": '',
    "author.location.x": '',
    "author.location.y": '',
    "author.location.z": '',
    "author.location.name": ''
  });
  const [lessMaximalPointFlag, setlessMaximalPointFlag] = useState(false)

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

  const baseCatch = async (error) => {
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

  const getLabsData = async () => {
    const params = constructQueryParams(filterFields, activeSizePerPage, activePage, activeSortField);
    const host_path = !lessMaximalPointFlag ? first_host : `${first_host}/less_maximum_point/${filterFields.maximumPoint}`
    return axios.get(
      host_path,
      { params }).then(data => {
        parseString(data.data, { explicitArray: false, ignoreAttrs: true }, function (err, result) {
          setTotalSize(parseInt(result.labworks_result.totalLabWorks))
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
          baseCatch(error)
        })
  }

  const createNewLab = async (object) => {
    let xmlObject = xmlBuilder.buildObject(object);
    axios.post(
      first_host, xmlObject, {headers: {'Content-Type': 'application/xml'}}
    ).then(data => {
      getLabsData();
    }).catch(function (error) {
      baseCatch(error)
    })
  }

  const updateNewLab = async (object) => {
    let xmlObject = xmlBuilder.buildObject(object);
    axios.put(
      `${first_host}/${object.labWork.id}`, xmlObject, {headers: {'Content-Type': 'application/xml'}}
    ).then(data => {
      getLabsData();
    })
      .catch(function (error) {
        baseCatch(error)
      })
  }

  const handleDelete = async (id) => {
    axios.delete(
      `${first_host}/${id}`
    ).then(data => {
      getLabsData();
    })
      .catch(function (error) {
        baseCatch(error)
      })
  }

  const searchLab = async (id) => {
    axios.get(
      `${first_host}/${id}`
    )
      .then(data => {
        parseString(data.data, { explicitArray: false, ignoreAttrs: true }, function (err, result) {
          setLab({
            ...lab,
            ...result.labWork
          })
          setShowLab(true)
        })
      })
      .catch(function (error) {
        baseCatch(error)
      })
  }

  const serchLabWithMinName = async () => {
    axios.get(
      `${first_host}/min_name`
    )
      .then(data => {
        parseString(data.data, { explicitArray: false, ignoreAttrs: true }, function (err, result) {
          setLab({
            ...lab,
            ...result.labWork
          })
          setShowLab(true)
        })
      })
      .catch(function (error) {
        baseCatch(error)
      })
  }

  const countPQM = async (pqm) => {
    axios.get(
      `${first_host}/count_personal_maximum/${pqm}`
    )
      .then(data => {
        catchInfo(data.data)
      })
      .catch(function (error) {
        baseCatch(error)
      })
  }

  const addLabWorkToDiscipline = async (disciplineId, labWorkId) => {
    axios.post(
        `${second_host}/${disciplineId}/${labWorkId}`
    )
        .then(data => {
          catchInfo(data.data)
        })
        .catch(function (error) {
          baseCatch(error)
        })
  }

  const increaseDifficulty = async (steps, labWorkId) => {
    axios.put(
        `${third_host}/${labWorkId}/difficulty/increase/${steps}`
    )
        .then(data => {
          catchInfo(data.data)
          getLabsData()
        })
        .catch(function (error) {
          baseCatch(error)
        })
  }

  const handleSelect = (disciplineId, labWorkId) => {
    addLabWorkToDiscipline(disciplineId, labWorkId)
  }

  const handleFilter = async () => {
    getLabsData()
  }

  useEffect(() => {
    if (firstQuery) {
      getLabsData();
      getDisciplineData();
    }
  }, [activeSortField, activePage, activeSizePerPage]);

  const handleTableChange = (type, { page, sizePerPage, filters, sortField, sortOrder, cellEdit }) => {

    //Handle sort
    setFirstQuery(true)
    setActiveSortField(constructSortField(sortField))

    //Handle cell edit
    if (type === 'cellEdit') {
      const { rowId, dataField, newValue } = cellEdit;


      for (let row of labs) {
        if (row.id === rowId) {
          let copiedLab = JSON.parse(JSON.stringify(row));
          constructUpdateObject(copiedLab, dataField, newValue)
          copiedLab = { labWork: copiedLab }
          updateNewLab(copiedLab)
          break;
        }
      }
    }

    // Handle column filters
    const newFilterFields = {};
    for (const key of Object.keys(filterFields)) {
      newFilterFields[key] = ''
    }
    setlessMaximalPointFlag(false)
    for (const dataField in filters) {
      const { filterVal, filterType, comparator } = filters[dataField];
      if (filterType === "NUMBER") {
        if (dataField === "maximumPoint" && filterVal.comparator === Comparator.LT) {
          setlessMaximalPointFlag(true)
        }
        newFilterFields[dataField] = filterVal.number;
      } else if (filterType === "DATE") {
        newFilterFields[dataField] = moment(filterVal.date).format('DD.MM.yyyy')
      } else {
        newFilterFields[dataField] = filterVal;
      }
    }
    setFilterFields({
      ...filterFields,
      ...newFilterFields
    })

    //Handle pagination
    setActivePage(page);
    setActiveSizePerPage(sizePerPage);
  }

  const sortCaretSpan = (order, column) => {
    if (!order) return (<span>&nbsp;&nbsp;</span>);
    else if (order === 'asc') return (<span>&nbsp;&nbsp;^</span>);
    else if (order === 'desc') return (<span>&nbsp;&nbsp;^</span>);
    return null;
  }

  const columns = [
    { dataField: "id", text: "id", sort: true, sortCaret: sortCaretSpan },
    { dataField: "name", text: "name", filter: textFilter(), sort: true, sortCaret: sortCaretSpan },
    {
      dataField: "coordinates.x", text: "coordinatesX", filter: numberFilter({
        withoutEmptyComparatorOption: true,
        comparatorStyle: { display: 'none' },
      }), sort: true, sortCaret: sortCaretSpan
    },
    {
      dataField: "coordinates.y", text: "coordinatesY", filter: numberFilter({
        withoutEmptyComparatorOption: true,
        comparatorStyle: { display: 'none' },
      }), sort: true, sortCaret: sortCaretSpan
    },
    {
      dataField: "creationDate", text: "creationDate",
      editor: {
        type: Type.DATE
      },
      filter: dateFilter({
        comparatorStyle: { display: 'none' },
      }),
      sort: true, sortCaret: sortCaretSpan
    },
    {
      dataField: "minimalPoint", text: "minimalPoint", filter: numberFilter({
        withoutEmptyComparatorOption: true,
        comparatorStyle: { display: 'none' },
      }), sort: true, sortCaret: sortCaretSpan
    },
    {
      dataField: "maximumPoint", text: "maximumPoint", filter: numberFilter({
        comparators: [Comparator.EQ, Comparator.LT],
        withoutEmptyComparatorOption: true,
        // comparatorStyle: { display: 'none' },
      }), sort: true, sortCaret: sortCaretSpan
    },
    {
      dataField: "personalQualitiesMaximum", text: "personalQualitiesMaximum", filter: numberFilter({
        comparators: Comparator.EQ,
        withoutEmptyComparatorOption: true,
        comparatorStyle: { display: 'none' },
      }), sort: true, sortCaret: sortCaretSpan
    },
    {
      dataField: "difficulty", text: "difficulty",
      editor: {
        type: Type.SELECT,
        options: [{
          value: 'VERY_EASY',
          label: 'VERY_EASY'
        }, {
          value: 'EASY',
          label: 'EASY'
        }, {
          value: 'INSANE',
          label: 'INSANE'
        }, {
          value: 'HOPELESS',
          label: 'HOPELESS'
        }, {
          value: 'TERRIBLE',
          label: 'TERRIBLE'
        }]
      },
      formatter: cell => options[cell],
      filter: selectFilter({
        options: options
      }),
      sort: true, sortCaret: sortCaretSpan
    },
    {
      dataField: "Increase Difficulty",
      text: "Increase Difficulty",
      editable: (cell, row, rowIndex, colIndex) => {
        return false;
      },
      formatter: (cellContent, row) => {
        return (
            <Dropdown>
              <DropdownButton
                  variant="secondary"
                  id="dropdown-basic"
                  onSelect={(e) => increaseDifficulty(e, row.id)}
                  title="Increase Difficulty"
              >
                <Dropdown.Item eventKey={1}>1</Dropdown.Item>
                <Dropdown.Item eventKey={2}>2</Dropdown.Item>
                <Dropdown.Item eventKey={3}>3</Dropdown.Item>
                <Dropdown.Item eventKey={4}>4</Dropdown.Item>
              </DropdownButton>
            </Dropdown>
        );
      },
    },
    { dataField: "author.name", text: "authorName", filter: textFilter(), sort: true },
    {
      dataField: "author.weight", text: "authorWeight", filter: numberFilter({
        comparators: Comparator.EQ,
        withoutEmptyComparatorOption: true,
        comparatorStyle: { display: 'none' },
      }), sort: true, sortCaret: sortCaretSpan
    },
    {
      dataField: "author.location.x", text: "locationX", filter: numberFilter({
        comparators: Comparator.EQ,
        withoutEmptyComparatorOption: true,
        comparatorStyle: { display: 'none' },
      }), sort: true, sortCaret: sortCaretSpan
    },
    {
      dataField: "author.location.y", text: "locationY", filter: numberFilter({
        comparators: Comparator.EQ,
        withoutEmptyComparatorOption: true,
        comparatorStyle: { display: 'none' },
      }), sort: true, sortCaret: sortCaretSpan
    },
    {
      dataField: "author.location.z", text: "locationZ", filter: numberFilter({
        comparators: Comparator.EQ,
        withoutEmptyComparatorOption: true,
        comparatorStyle: { display: 'none' },
      }), sort: true, sortCaret: sortCaretSpan
    },
    { dataField: "author.location.name", text: "locationName", filter: textFilter(), sort: true, sortCaret: sortCaretSpan },
    {
      dataField: "remove",
      text: "Delete",
      editable: (cell, row, rowIndex, colIndex) => {
        return false;
      },
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
    {
      dataField: "add to discipline",
      text: "Add to Discipline",
      editable: (cell, row, rowIndex, colIndex) => {
        return false;
      },
      formatter: (cellContent, row) => {
        return (
            <Dropdown>
              <DropdownButton
                  variant="success"
                  id="dropdown-basic"
                  onSelect={(e) => handleSelect(e, row.id)}
                  title="Add to Discipline"
              >
                {disciplines.map(item => {
                  return (<Dropdown.Item eventKey={item.id}>{item.name}</Dropdown.Item>);
                })}
              </DropdownButton>
            </Dropdown>
        );
      },
    },
  ]

  return (
    <div className="mt-5">
      <br />
      <SpecialFuncs handleFilter={handleFilter} searchLab={searchLab} getMinName={serchLabWithMinName} countPQM={countPQM} />
      <AddNewLab createNewLab={createNewLab} catchError={catchError} catchInfo={catchInfo}/>
      <InfoModal setShow={setShowError} show={showError} message={message} />
      <LabModal setShow={setShowLab} show={showLab} labWork={lab} />
      <div style={{ overFlowX: 'auto' }} className="m-3">
        <BootstrapTable
          remote
          classes="w-auto"
          keyField="id"
          data={labs}
          columns={columns}
          cellEdit={cellEditFactory({ mode: 'dbclick' })}
          rowStyle={{ whiteSpace: 'nowrap', wordWrap: 'break-word' }}
          filter={filterFactory()}
          pagination={paginationFactory({ activePage, activeSizePerPage, totalSize, sizePerPageRenderer: sizePerPageRenderer })}
          onTableChange={handleTableChange}
          defaultSorted={defaultSorted}
        />
      </div>
    </div>
  )
}

export default LabWorkTable
