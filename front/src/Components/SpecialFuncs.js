import React, { useState } from 'react';
import { Button, Form } from 'react-bootstrap'

const SpecialFuncs = (props) => {

    const [id, setId] = useState("")
    const [personalQualitiesMaximum, setPersonalQualitiesMaximum] = useState("")

    const setIdValue = (e) => {
        setId(e.target.value)
    }

    const setPQMValue = (e) => {
        setPersonalQualitiesMaximum(e.target.value)
    }

    const searchLab = () => {
        props.searchLab(id)
    }

    const countPersonalQM = () => {
        props.countPQM(personalQualitiesMaximum)
    }

    const getLabMinName = () => {
        props.getMinName()
    }

    return (
        <div className="d-flex justify-content-center">
            <Button
                className="m-3"
                variant="primary"
                onClick={props.handleFilter}
                size="lg"
            >
                Filter
            </Button>
            <Form.Control
                className="ms-3 mt-3 mb-3 me-1 w-25"
                placeholder="Enter id"
                type="number"
                onChange={setIdValue}
            />
            <Button
                className="me-3 mt-3 mb-3 ms-1"
                variant="dark"
                type="submit"
                size="lg"
                onClick={searchLab}
                >
                Search
            </Button>
            <Button
                className="me-3 mt-3 mb-3 ms-1"
                variant="warning"
                type="submit"
                size="lg"
                onClick={getLabMinName}
                >
                Get Lab with Min Name
            </Button>
            <Form.Control
                className="ms-3 mt-3 mb-3 me-1 w-25"
                placeholder="Enter personal QM"
                type="number"
                onChange={setPQMValue}
            />
            <Button
                className="me-3 mt-3 mb-3 ms-1"
                variant="dark"
                type="submit"
                size="lg"
                onClick={countPersonalQM}
                >
                Count
            </Button>
        </div>
    )
}

export default SpecialFuncs
