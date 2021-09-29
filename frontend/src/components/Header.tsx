import React from "react"

function Header(): JSX.Element {
    return (
        <div className="is-small hero is-primary">
            <div className="hero-body">
                <p className="title is-1 has-text-centered">
                    Steak Saver
                </p>
            </div>
        </div>
    )
}

export default React.memo(Header)