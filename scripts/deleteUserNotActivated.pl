#!/usr/bin/perl
use strict;
use warnings;
use DBI;
use Time::localtime;

my $year = localtime->year() + 1900;
my $month = localtime->mon() + 1;
my $day = localtime->mday();

print "$day-$month-$year - Delete Users Not Activated \n";

my $db1 = "MBAL_server";
my $host = "localhost";
my $port = "3306";
my $user = "valentin";
my $pw = "Frigoule34";

my $dsn = "DBI:mysql:database=$db1;host=$host;port=$port";
my $dbh = DBI->connect($dsn, $user, $pw,
    { RaiseError => 1, AutoCommit => 1 });

# delete records from table
my $sql = "SELECT id,mail,DATEDIFF(NOW(), creation_date) AS date_diff FROM user WHERE is_activated != \"Activated\"";
my $sth = $dbh->prepare($sql);          # prepare the query
$sth->execute(); # execute the query

my @row;

while (@row = $sth->fetchrow_array()) {  # retrieve one row
    my ($id, $mail, $date_diff) = @row;
    if (defined($mail) && $mail ne "") {
        if ($date_diff <= 3) {
            my $rolesToDelete = "DELETE FROM user_roles WHERE user_id=$id";
            my $userToDelete = "DELETE FROM user WHERE mail=\"$mail\"";

            my $deleteRolesStatement = $dbh->prepare($rolesToDelete);
            $deleteRolesStatement->execute();
            $deleteRolesStatement->finish;

            my $deleteUserStatement = $dbh->prepare($userToDelete);
            $deleteUserStatement->execute();
            print "User $mail has been deleted ! \n";
            $deleteUserStatement->finish;

            print "\n";
        }
    }else{
        print "No users to delete ! \n";
    }
}

print "Users have been deleted ! \n";

$sth->finish;
$dbh->disconnect;

exit;

