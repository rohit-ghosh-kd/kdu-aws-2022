All the functionalities used in modelling IPL into an object oriented program here are implemented by following the various logic methadologies below.

* Taking an assumption that the total no of dismissals for a player is equivalent to the total no of innings played by a player.

* total_no_of_innings = total_no_of_dismissals
* average = total_runs_scored / total_no_of_dismissals
=> average = total_runs_scored / total_no_of_innings
=> total_no_of_innings = total_runs_scored / average

* total_no_of_innings = total_runs_scored / average

* strike_rate = (total_runs_scored/total_number_of_balls_played) * 100
=> total_number_of_balls_played = (total_runs_scored * 100) / strike_rate
=> predicted_number_of_balls_per_innings = total_number_of_balls_played / total_no_of_innings

* predicted_number_of_balls_per_innings = total_number_of_balls_played / total_no_of_innings

* runs_per_ball = total_runs_scored / total_number_of_balls_played
* predicted_runs_per_innings = predicted_number_of_balls_per_innings * runs_per_ball

* runs_per_innings = total_runs_scored / total_no_of_innings

To calculate the highest score of every team when on an hypothetical condition they play their 11 best batsmen, the average runs a player score in an inning is used from the formulas listed above. Then these values are added to find the highest runs a team can make.
When predicting the total score for the two highest scoring teams, the number of balls left in the innings is kept track of whenever we add the runs score by a player in the innings. If the total number of balls left in the innings is more than the number of balls the player usually plays in an inning, then the computation is done on all of the balls he plays on average. Otherwise, the computation is done only on the rest number of balls.

