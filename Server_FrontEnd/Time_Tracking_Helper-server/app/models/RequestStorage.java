package models;

public class RequestStorage {

	/*
	 * TODO: metody do obsługi zapytań o pokazanie harmonogramów:
	 * -zablokowac zapytania z tym samym nickiem (np. "Kruk07" "Kruk07")
	 * -zablokowac powtorne wysylanie tych samych zapytan, jeśli zostaly wyslane wczesniej
	 * -zablokowac zapytania, jesli istnieja takie same krotki w priveleges
	 * - zablokowac zapytanie, jesli nie istnieje nick w bazie "User"
	 * 
	 * 
	 * Po zaakceptowaniu zaproszenia usunac krotkę z tabeli Request i dodac ja do tabeli Priveleges
	 * 
	 * obsluga zapytan do frontendu:
	 * "Users that request for adding you"- Select Request po "userto"
	 * "Users that you request for adding"- Select Request po "userfrom"
	 * "Users that added you" - Select Privileges po "userto"
	 * "Users that you added"- Select Privileges po "userfrom"
	 * 
	 * usuwanie- powinno byc zrobione i dla Priveleges i dla Request, w zależności od akcji w froncie
	 */
}
