// Venter på at HTML'en er færdig med at loade
document.addEventListener('DOMContentLoaded', function () {
    const cardRadio = document.getElementById('method-card');
    const mobileRadio = document.getElementById('method-mobile');
    const cardFields = document.getElementById('card-fields');
    const mpFields = document.getElementById('mp-fields');

    // Funktion til at vise/skjule felter afhængig af valgt metode
    function toggleFields() {
        if (cardRadio.checked) {
            cardFields.style.display = 'block';
            mpFields.style.display = 'none';
        } else if (mobileRadio.checked) {
            cardFields.style.display = 'none';
            mpFields.style.display = 'block';
        }
    }

    // Vis korrekt felt ved indlæsning
    toggleFields();

    // Når brugeren ændrer valg
    cardRadio.addEventListener('change', toggleFields);
    mobileRadio.addEventListener('change', toggleFields);
});