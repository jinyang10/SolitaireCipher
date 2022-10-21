package assignment2;

import java.util.Random;

public class Deck {
 public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
 public static Random gen = new Random();

 public int numOfCards; // contains the total number of cards in the deck
 public Card head; // contains a pointer to the card on the top of the deck

 /* 
  * Initializes a Deck object using the inputs provided
  */
 public Deck(int numOfCardsPerSuit, int numOfSuits) {

  if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13 || numOfSuits < 1 || numOfSuits > suitsInOrder.length) {
   throw new IllegalArgumentException();

  } else {

   Card cur = null;

   for (int suit=0; suit<numOfSuits; suit++) {
    for (int rank=1; rank<=numOfCardsPerSuit; rank++) {

     if (suit == 0 && rank == 1) {
      head = new PlayingCard(suitsInOrder[0], 1);
      cur = head;
     } else {

      Card card = new PlayingCard(suitsInOrder[suit], rank);
      cur.next = card;
      card.prev = cur;
      cur = cur.next;
     }
    }
   }

   Card rj = new Joker("red");
   Card bj = new Joker("black");
   cur.next = rj;
   rj.prev = cur;
   rj.next = bj;
   bj.prev = rj;
   bj.next = head;
   head.prev = bj;
  }
  numOfCards = numOfSuits * numOfCardsPerSuit + 2;
 }

 /* 
  * Implements a copy constructor for Deck using Card.getCopy().
  * This method runs in O(n), where n is the number of cards in d.
  */
 public Deck(Deck d) {

  numOfCards = d.numOfCards;

  if (d.numOfCards == 1) {
   Card copy = d.head.getCopy();
   head = copy;
   head.next = head;
   head.prev = head;

  } else {
   Card dCur = d.head;
   head = dCur.getCopy();
   Card cur = head;

   for (int i=1; i<numOfCards; i++) {
    cur.next = dCur.next.getCopy();
    cur.next.prev = cur;

    cur = cur.next;
    dCur = dCur.next;
   }
   head.prev = cur;
   cur.next = head;
  }

 }

 /*
  * For testing purposes we need a default constructor.
  */
 public Deck() {}

 /* 
  * Adds the specified card at the bottom of the deck. This
  * method runs in $O(1)$. 
  */
 public void addCard(Card c) {
  if (numOfCards < 1) {
   head = c;
   head.next = head;
   head.prev = head;
   numOfCards = 1;

  } else {
   head.prev.next = c;
   c.prev = head.prev;
   head.prev = c;
   c.next = head;
   numOfCards +=1;
  }

 }

 /*
  * Shuffles the deck using the Fisher Yates algorithm
  * This method runs in O(n) and uses O(n) space, where n is the total 
  * number of cards in the deck.
  */
 public void shuffle() {
  if (numOfCards < 1) {
   return;
  }
  Card[] arrCopies = new Card[numOfCards];
  Card cur = head;

  //copy all cards into array
  for (int i=0; i<numOfCards; i++) {
   arrCopies[i] = cur;
   cur = cur.next;
  }

  //shuffle
  for (int i=numOfCards-1; i>0; i--) {
   int j = gen.nextInt(i+1);
   Card temp = arrCopies[i];
   arrCopies[i] = arrCopies[j];
   arrCopies[j] = temp;
  }



  //rebuild deck
  head = arrCopies[0];
  cur = head;
  for (int i=1; i<numOfCards; i++) {
   cur.next = arrCopies[i];
   cur.next.prev = cur;
   cur = cur.next;
  }
  head.prev = cur;
  cur.next = head;


 }

 /*
  * Returns a reference to the joker with the specified color in
  * the deck. This method runs in O(n), where n is the total number of 
  * cards in the deck. 
  */
 public Joker locateJoker(String color) {
  Card cur = head;
  for (int i=0; i<numOfCards; i++) {
   if (cur instanceof Joker) {
    if (((Joker) cur).redOrBlack.equals(color)) {
     return (Joker) cur;
    }
   }
   cur = cur.next;
  }
  return null;
 }

 /*
  * Moved the specified Card, p positions down the deck. You can
  * assume that the input Card does belong to the deck (hence the deck is
  * not empty). This method runs in O(p).
  */
 public void moveCard(Card c, int p) {
  Card cur = c;
  Card tempPrev = cur.prev;

  for (int i=0; i<p; i++) {
   cur = cur.next;
   if (cur == c) {
    cur = cur.next;
   }
  }
  tempPrev.next = c.next;
  c.next.prev = tempPrev;
  Card curtempNext = cur.next;
  cur.next = c;
  c.prev = cur;
  c.next = curtempNext;
  curtempNext.prev = c;

 }

 /*
  * Performs a triple cut on the deck using the two input cards.
  * assume that the input cards belong to the deck and the first one is
  * nearest to the top of the deck. This method runs in O(1)
  */
 public void tripleCut(Card firstCard, Card secondCard) {
  Card firstPrev = firstCard.prev;
  Card secondNext = secondCard.next;
  Card h = head;
  Card last = head.prev;

  last.next = firstCard;
  firstCard.prev = last;
  secondCard.next = h;
  h.prev = secondCard;
  firstPrev.next = secondNext;
  secondNext.prev = firstPrev;
  head = secondNext;
 }

 /*
  * Performs a count cut on the deck. Note that if the value of the
  * bottom card is equal to a multiple of the number of cards in the deck, 
  * then the method should not do anything. This method runs in O(n).
  */
 public void countCut() {
  Card bottomCard = head.prev;
  int value = bottomCard.getValue();
  if (value % numOfCards == 0 || value == numOfCards - 1) {
   return;
  }
  if (value > numOfCards) {
   value = numOfCards - value;
  }

  Card firstCard = head;
  Card cur = head;

  for (int i=1; i<value; i++) {
   cur = cur.next;
  }
  Card secondCard = cur;
  Card tempPrev = bottomCard.prev;
  Card newHead = secondCard.next;

  secondCard.next = bottomCard;
  bottomCard.prev = secondCard;
  firstCard.prev = tempPrev;
  tempPrev.next = firstCard;
  head = newHead;
  newHead.prev = bottomCard;
 }

 /*
  * Returns the card that can be found by looking at the value of the
  * card on the top of the deck, and counting down that many cards. If the 
  * card found is a Joker, then the method returns null, otherwise it returns
  * the Card found. This method runs in O(n).
  */
 public Card lookUpCard() {
  int value = head.getValue();
  Card cur = head;
  
  for (int i=0; i<value; i++) {
   cur = cur.next;
  }
  if (cur instanceof Joker) {
   return null;
  }
  return cur;
 }

 /*
  * Uses the Solitaire algorithm to generate one value for the keystream
  * using this deck. This method runs in O(n).
  */
 public int generateNextKeystreamValue() {
  //locate red joker, move it one card down
  Card redjoker = locateJoker("red");
  if (head.prev == redjoker) {
   moveCard(redjoker, 2);
  } else {
   moveCard(redjoker, 1);
  }

  //locate black joker, move 2 cards down
  Card blackJoker = locateJoker("black");
  if (head.prev.prev == blackJoker) {
   moveCard(blackJoker, 3);
  } else {
   moveCard(blackJoker, 2);
  }

  Card cur = head;
  String jokerColor = "";
  for (int i=1; i<numOfCards; i++) {
   cur = cur.next;
   if (cur instanceof Joker) {
    jokerColor = ((Joker) cur).redOrBlack;
    break;
   }
  }

  Card firstJoker, secondJoker;

  if (jokerColor.equals("black")) {
   firstJoker = blackJoker;
   secondJoker = redjoker;
  } else {
   firstJoker = redjoker;
   secondJoker = blackJoker;
  }

  tripleCut(firstJoker, secondJoker);
  countCut();

  Card card;
  while (true) {
   card = lookUpCard();
   if (card == null) {
    continue;
   }
   return card.getValue();
  }

 }


 public abstract class Card { 
  public Card next;
  public Card prev;

  public abstract Card getCopy();
  public abstract int getValue();

 }

 public class PlayingCard extends Card {
  public String suit;
  public int rank;

  public PlayingCard(String s, int r) {
   this.suit = s.toLowerCase();
   this.rank = r;
  }

  public String toString() {
   String info = "";
   if (this.rank == 1) {
    //info += "Ace";
    info += "A";
   } else if (this.rank > 10) {
    String[] cards = {"Jack", "Queen", "King"};
    //info += cards[this.rank - 11];
    info += cards[this.rank - 11].charAt(0);
   } else {
    info += this.rank;
   }
   //info += " of " + this.suit;
   info = (info + this.suit.charAt(0)).toUpperCase();
   return info;
  }

  public PlayingCard getCopy() {
   return new PlayingCard(this.suit, this.rank);   
  }

  public int getValue() {
   int i;
   for (i = 0; i < suitsInOrder.length; i++) {
    if (this.suit.equals(suitsInOrder[i]))
     break;
   }

   return this.rank + 13*i;
  }

 }

 public class Joker extends Card{
  public String redOrBlack;

  public Joker(String c) {
   if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black")) 
    throw new IllegalArgumentException("Jokers can only be red or black"); 

   this.redOrBlack = c.toLowerCase();
  }

  public String toString() {
   //return this.redOrBlack + " Joker";
   return (this.redOrBlack.charAt(0) + "J").toUpperCase();
  }

  public Joker getCopy() {
   return new Joker(this.redOrBlack);
  }

  public int getValue() {
   return numOfCards - 1;
  }

  public String getColor() {
   return this.redOrBlack;
  }
 }

}
