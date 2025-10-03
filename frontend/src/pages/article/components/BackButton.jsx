const BackButton = () => {
    const isMobile = /Mobi|Android/i.test(navigator.userAgent);
    if (isMobile) return null;

    return (
        <button
            onClick={() => window.history.back()}

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
                outline: "none",
                boxShadow: "0 2px 8px rgba(0,0,0,0.15)",
                fontSize: "20px",
                cursor: "pointer",
                color: "#fff",
                fontWeight: "bold",
                backgroundColor: "#4d4d4d"
            }}
            aria-label="Back to Articles"
        >
            &#8592;
        </button>
    )
}

export default BackButton
