const BackButton = () => {
    const isMobile = /Mobi|Android/i.test(navigator.userAgent);
    if (isMobile) return null;

    return (
        <a
            onClick={() => window.history.back()}

            className="btn btn-primary"
            style={{
                position: "fixed",
                left: "24px",
                bottom: "24px",
                width: "56px",
                height: "56px",
                borderRadius: "50%",
                display: "flex",
                alignItems: "center",
                justifyContent: "center",
                boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
                fontSize: "20px",
                cursor: "grab",
                color: "#fff",
                fontWeight: "bold",
                backgroundColor: "#4d4d4d"
            }}
            aria-label="Back to Articles"
        >
            &#8592;
        </a>
    )
}

export default BackButton
