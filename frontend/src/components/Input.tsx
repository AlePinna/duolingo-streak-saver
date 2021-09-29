import { camelCase } from "lodash";
import { FieldValues, UseFormRegister } from "react-hook-form";

type InputPropTypes = {
    error: { [x: string]: any },
    label: string,
    register: UseFormRegister<FieldValues>,
    required?: boolean | string,
    type: 'text' | 'password'
}

export default function Input({ error, label, register, type, required }: InputPropTypes): JSX.Element {
    const reference = camelCase(label)
    const inputClass = error[reference]?.message ?
        'input is-danger' : 'input is-info'
    return (
        <div className="field ">
            <label className="label" >{label}</label>
            <div className="control ">
                <input type={type} className={inputClass} placeholder={label} {...register(reference, { required })} />
            </div>
            {error[reference]?.message && <p className="help is-danger">{error[reference]?.message}</p>}
        </div>)
}