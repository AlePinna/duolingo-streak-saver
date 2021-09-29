
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from "yup";
import { Input } from './index'



type FormInputType = {
    userName: string;
    password: string;
};
const schema: yup.SchemaOf<FormInputType> = yup.object().shape({
    userName: yup.string().required("Required"),
    password: yup.string().required("Required"),

});


export default function Form() {
    const { register, handleSubmit, formState: { errors } } = useForm({ resolver: yupResolver(schema) });

    const onSubmit = handleSubmit(data => {
        alert(JSON.stringify(data));
    });

    return (
        <div className='columns is-desktop is-centered m-4'>
            <form onSubmit={onSubmit} className='column is-half'>
                <Input type='text' label="User Name" register={register} required error={errors} />
                <Input type='password' label="Password" register={register} required error={errors} />
                <div className='field is-grouped is-grouped-right'>
                    <div className='control'>
                        <button className="button is-link">Submit</button>
                    </div>
                </div>
            </form >
        </div>
    )
}